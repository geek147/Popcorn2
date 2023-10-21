package com.example.popcorn.data.datasource.mapper

import com.example.popcorn.data.datasource.remote.response.UserReviewResponse
import com.example.popcorn.domain.model.UserReview

class UserReviewRemoteMapper : BaseMapperRepository<UserReviewResponse.UserReviewItem
        , UserReview> {
    override fun transform(item: UserReviewResponse.UserReviewItem): UserReview = UserReview(
        id = item.id,
        author = item.author.orEmpty(),
        content = item.content.orEmpty(),
        createdAt = item.createdAt.orEmpty()
    )

    override fun transformToRepository(item: UserReview): UserReviewResponse.UserReviewItem =
        UserReviewResponse.UserReviewItem(
            id = item.id,
            author = item.author,
            content = item.content,
            authorDetails = null,
            createdAt = item.createdAt,
            updatedAt = "",
            url = ""
        )
}