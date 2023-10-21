package com.example.popcorn.data.usecase

import com.example.popcorn.data.dispatchers.CoroutineDispatchers
import com.example.popcorn.domain.model.UserReview
import com.example.popcorn.domain.repository.MovieRepository
import com.example.popcorn.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetUserReviewUseCase @Inject constructor(
    private val repository: MovieRepository,
    private val ioDispatchers: CoroutineDispatchers,
) {

    suspend operator fun invoke(params: Params): Flow<Result<List<UserReview>>> =
        repository.getUserReview(params.page, params.movieId).flowOn(ioDispatchers.io)

    class Params(val page: Int,val movieId: Int)
}
