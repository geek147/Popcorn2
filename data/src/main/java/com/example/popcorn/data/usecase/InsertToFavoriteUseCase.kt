package com.example.popcorn.data.usecase

import com.example.popcorn.data.dispatchers.CoroutineDispatchers
import com.example.popcorn.domain.model.Movie
import com.example.popcorn.domain.repository.MovieRepository
import com.example.popcorn.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class InsertToFavoriteUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val ioDispatchers: CoroutineDispatchers
) {

    suspend operator fun invoke(params: Params): Flow<Result<Result.SuccessNoReturn>> =
        movieRepository.insertFavoriteMovie(params.movie).flowOn(ioDispatchers.io)

    class Params(val movie: Movie)
}
