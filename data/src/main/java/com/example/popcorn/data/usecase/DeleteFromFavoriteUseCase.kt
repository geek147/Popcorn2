package com.example.popcorn.data.usecase

import com.example.popcorn.data.dispatchers.CoroutineDispatchers
import com.example.popcorn.domain.repository.MovieRepository
import com.example.popcorn.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeleteFromFavoriteUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val ioDispatchers: CoroutineDispatchers
) {

    suspend operator fun invoke(params: Int): Flow<Result<Result.SuccessNoReturn>> =
        movieRepository.deleteFavoriteMovie(params).flowOn(ioDispatchers.io)

}
