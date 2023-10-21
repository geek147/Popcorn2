package com.example.popcorn.util

import com.example.popcorn.domain.model.Genre
import com.example.popcorn.domain.model.Movie
import com.example.popcorn.domain.model.UserReview


sealed class Intent {
    object GetFavorites : Intent()
    data class GetMovieByGenre(val withGenre: Int): Intent()
    data class GetUserReview(val movieId: Int): Intent()
    data class LoadNextMovieByGenre(val page: Int, val withGenre: Int) : Intent()
    data class LoadNextUserReview(val page: Int, val movieId: Int) : Intent()
    data class SaveToFavorite(val movie: Movie) : Intent()
    data class RemoveFromFavorite(val id: Int) : Intent()
}

data class State(
    val showLoading: Boolean = false,
    val listFavorite: List<Movie> = listOf(),
    val listGenre: List<Genre> = listOf(),
    val listMovieByGenre: List<Movie> = listOf(),
    val listUserReview: List<UserReview> = listOf(),
    val viewState: ViewState = ViewState.Idle
)

sealed class ViewState {
    object Idle : ViewState()
    object SuccessFirstInit : ViewState()
    object EmptyListFirstInit : ViewState()
    object EmptyListLoadMore : ViewState()
    object SuccessLoadMore : ViewState()
    object ErrorFirstInit : ViewState()
    object ErrorLoadMore : ViewState()
    object SuccessFirstInitGenre : ViewState()
    object EmptyListFirstInitGenre : ViewState()
    object ErrorFirstInitGenre : ViewState()
    object SuccessFirstInitMovieByGenre : ViewState()
    object EmptyListFirstInitMovieByGenre : ViewState()
    object EmptyListLoadMoreMovieByGenre : ViewState()
    object SuccessLoadMoreMovieByGenre : ViewState()
    object ErrorFirstInitMovieByGenre : ViewState()
    object ErrorLoadMoreMovieByGenre : ViewState()
    object SuccessFirstInitUserReview : ViewState()
    object EmptyListFirstInitUserReview : ViewState()
    object EmptyListLoadMoreUserReview : ViewState()
    object SuccessLoadMoreUserReview : ViewState()
    object ErrorFirstInitUserReview : ViewState()
    object ErrorLoadMoreUserReview : ViewState()
}
