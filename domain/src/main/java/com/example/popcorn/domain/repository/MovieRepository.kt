package com.example.popcorn.domain.repository

import com.example.popcorn.domain.util.Result
import com.example.popcorn.domain.model.Movie
import com.example.popcorn.domain.model.UserReview
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getFavoriteMovies(): Flow<Result<List<Movie>>>

    suspend fun insertFavoriteMovie(movie: Movie): Flow<Result<Result.SuccessNoReturn>>

    suspend fun deleteFavoriteMovie(id: Int): Flow<Result<Result.SuccessNoReturn>>

    suspend fun getMovieByGenre(
        page: Int,
        withGenre: Int
    ): Flow<Result<List<Movie>>>

    suspend fun getUserReview(
        page: Int,
        movieId: Int
    ): Flow<Result<List<UserReview>>>

}


