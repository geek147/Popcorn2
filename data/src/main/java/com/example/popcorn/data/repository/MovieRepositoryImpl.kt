package com.example.popcorn.data.repository

import android.util.Log
import com.envious.data.util.Constants
import com.example.popcorn.data.BuildConfig
import com.example.popcorn.data.datasource.local.dao.FavoriteDao
import com.example.popcorn.data.datasource.local.model.FavoriteEntity
import com.example.popcorn.data.datasource.mapper.MovieItemLocalMapper
import com.example.popcorn.data.datasource.mapper.MovieItemRemoteMapper
import com.example.popcorn.data.datasource.mapper.UserReviewRemoteMapper
import com.example.popcorn.data.datasource.remote.MovieApiService
import com.example.popcorn.domain.model.Movie
import com.example.popcorn.domain.model.UserReview
import com.example.popcorn.domain.repository.MovieRepository
import com.example.popcorn.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApiService: MovieApiService,
    private val favoriteDao: FavoriteDao
) : MovieRepository {

    override suspend fun getFavoriteMovies(): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)
        val localMapper = MovieItemLocalMapper()
        val itemsFavorite = favoriteDao.getAllFavorites()
        val listData = mutableListOf<Movie>()

        itemsFavorite.forEach {
            listData.add(localMapper.transform(it!!))
        }
        emit(Result.Success(listData.toList()))
    }

    override suspend fun insertFavoriteMovie(movie: Movie): Flow<Result<Result.SuccessNoReturn>> = flow {
        favoriteDao.insert(
            FavoriteEntity(
                id = movie.id,
                adult = movie.adult,
                backdropPath = movie.backdropPath,
                originalLanguage = movie.originalLanguage,
                originalTitle = movie.originalTitle,
                overview = movie.overview,
                popularity = movie.popularity,
                posterPath = movie.posterPath,
                releaseDate = movie.releaseDate,
                title = movie.title,
                video = movie.video,
            )
        )

    }

    override suspend fun deleteFavoriteMovie(id: Int): Flow<Result<Result.SuccessNoReturn>> = flow {
        favoriteDao.delete(id)
    }
    
    override suspend fun getMovieByGenre(page: Int, withGenre: Int): Flow<Result<List<Movie>>> = flow<Result<List<Movie>>> {
        emit(Result.Loading)
        val result = movieApiService.getListMovieByGenre(
                apiKey = BuildConfig.API_KEY,
                language = Constants.MOVIE_LANGUAGE,
                page = page,
                withGenre = withGenre
        )
        if (result.isSuccessful) {
            val remoteMapper = MovieItemRemoteMapper()
            val remoteData = result.body()
            val items = remoteData?.movieItems
             if (remoteData != null && !items.isNullOrEmpty()) {
                val listData = mutableListOf<Movie>()
                items.forEach {
                    val data = remoteMapper.transform(it!!)
                    data.isPopularMovie = true
                    listData.add(data)
                }

                 val localMapper = MovieItemLocalMapper()
                 val itemsFavorite = favoriteDao.getAllFavorites()
                 val listDataFavorite = mutableListOf<Movie>()

                 itemsFavorite.forEach {
                     listDataFavorite.add(localMapper.transform(it!!))
                 }


                for (movie in listData) {
                    for( movie2 in listDataFavorite) {
                        if(movie.id == movie2.id) movie.isLiked = true
                    }
                }
                emit(Result.Success(listData.toList()))
            } else {
                emit(Result.Success(emptyList()))
            }
        } else {
             emit(Result.Error(Exception("Invalid data/failure")))
        }
    }.catch { e ->
        Log.e("ApiCalls", "Call error: ${e.localizedMessage}", e.cause)
        emit(Result.Error(Exception(e.cause)))
    }

    override suspend fun getUserReview(page: Int, movieId: Int): Flow<Result<List<UserReview>>> = flow<Result<List<UserReview>>>{
        emit(Result.Loading)

        val result = movieApiService.getUserReview(
                apiKey = BuildConfig.API_KEY,
                language = Constants.MOVIE_LANGUAGE,
                page = page,
                movieId = movieId
            )
        if (result.isSuccessful) {
            val remoteMapper = UserReviewRemoteMapper()
            val remoteData = result.body()
            val items = remoteData?.results
            if (remoteData != null && !items.isNullOrEmpty()) {
                val listData = mutableListOf<UserReview>()
                items.forEach {
                    val data = remoteMapper.transform(it!!)
                    listData.add(data)
                }
                emit(Result.Success(listData.toList()))
            } else {
                emit(Result.Success(emptyList()))
            }
        } else {
           emit(Result.Error(Exception("Invalid data/failure")))
        }
    }.catch { e ->
        Log.e("ApiCalls", "Call error: ${e.localizedMessage}", e.cause)
        emit(Result.Error(Exception(e.cause)))
    }

}
