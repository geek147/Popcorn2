package com.example.popcorn.ui.viewmodel

import androidx.lifecycle.viewModelScope

import com.example.popcorn.base.BaseViewModel
import com.example.popcorn.data.usecase.DeleteFromFavoriteUseCase
import com.example.popcorn.data.usecase.GetFavoritesUseCase
import com.example.popcorn.data.usecase.GetMovieByGenreUseCase
import com.example.popcorn.data.usecase.GetUserReviewUseCase
import com.example.popcorn.data.usecase.InsertToFavoriteUseCase
import com.example.popcorn.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.example.popcorn.domain.util.Result
import com.example.popcorn.util.Intent
import com.example.popcorn.util.State
import com.example.popcorn.util.ViewState
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val insertToFavoriteUseCase: InsertToFavoriteUseCase,
    private val deleteFromFavoriteUseCase: DeleteFromFavoriteUseCase,
    private val getMovieByGenreUseCase: GetMovieByGenreUseCase,
    private val getUserReviewUseCase: GetUserReviewUseCase,
) : BaseViewModel<Intent, State>(State()) {

    override fun onIntentReceived(intent: Intent) {
        when (intent) {
            Intent.GetFavorites -> {
                getFavorites()
            }
            is Intent.RemoveFromFavorite -> removeFromFavorite(intent.id)
            is Intent.SaveToFavorite -> saveToFavorite(intent.movie)
            is Intent.GetMovieByGenre -> getListMovieByGenre(1, false, intent.withGenre)
            is Intent.GetUserReview -> getUserReview(1, intent.movieId, false)
            is Intent.LoadNextMovieByGenre -> getListMovieByGenre(intent.page, true, intent.withGenre)
            is Intent.LoadNextUserReview -> getUserReview(intent.page, intent.movieId, true)
        }
    }

    private fun getUserReview(page: Int, movieId: Int, isLoadMore: Boolean) {


        val param = GetUserReviewUseCase.Params(page,movieId)

        viewModelScope.launch {
            getUserReviewUseCase(param).collect {
                when (it) {
                    Result.Loading -> {
                        setState {
                            copy(
                                showLoading = true,
                            )
                        }
                    }
                    is Result.Success -> {
                        if (isLoadMore) {
                            if (it.data.isEmpty()) {
                                setState {
                                    copy(
                                        listUserReview = emptyList(),
                                        showLoading = false,
                                        viewState = ViewState.EmptyListLoadMoreUserReview
                                    )
                                }
                            } else {
                                setState {
                                    copy(
                                        listUserReview = it.data,
                                        showLoading = false,
                                        viewState = ViewState.SuccessLoadMoreUserReview
                                    )
                                }
                            }
                        } else {
                            if (it.data.isEmpty()) {
                                setState {
                                    copy(
                                        listUserReview = emptyList(),
                                        showLoading = false,
                                        viewState = ViewState.EmptyListFirstInitUserReview
                                    )
                                }
                            } else {
                                setState {
                                    copy(
                                        listUserReview = it.data,
                                        showLoading = false,
                                        viewState = ViewState.SuccessFirstInitUserReview
                                    )
                                }
                            }
                        }
                    }

                    is Result.Error -> {
                        if (isLoadMore) {
                            setState {
                                copy(
                                    listUserReview = emptyList(),
                                    showLoading = false,
                                    viewState = ViewState.ErrorLoadMoreUserReview
                                )
                            }
                        } else {
                            setState {
                                copy(
                                    listUserReview = emptyList(),
                                    showLoading = false,
                                    viewState = ViewState.ErrorFirstInitUserReview
                                )
                            }
                        }
                    }

                    else -> {}
                }
            }

        }
    }


    private fun getListMovieByGenre(page: Int, isLoadMore: Boolean, withGenre: Int) {
        val param = GetMovieByGenreUseCase.Params(page,withGenre)

        viewModelScope.launch {
            getMovieByGenreUseCase(param).collect {
                when(it) {
                    Result.Loading -> {
                        setState {
                            copy(
                                showLoading = true,
                            )
                        }
                    }
                    is Result.Success -> {
                        if (isLoadMore) {
                            if (it.data.isEmpty()) {
                                setState {
                                    copy(
                                        listMovieByGenre = emptyList(),
                                        showLoading = false,
                                        viewState = ViewState.EmptyListLoadMoreMovieByGenre
                                    )
                                }
                            } else {
                                setState {
                                    copy(
                                        listMovieByGenre = it.data,
                                        showLoading = false,
                                        viewState = ViewState.SuccessLoadMoreMovieByGenre
                                    )
                                }
                            }
                        } else {
                            if (it.data.isEmpty()) {
                                setState {
                                    copy(
                                        listMovieByGenre = emptyList(),
                                        showLoading = false,
                                        viewState = ViewState.EmptyListFirstInitMovieByGenre
                                    )
                                }
                            } else {
                                setState {
                                    copy(
                                        listMovieByGenre = it.data,
                                        showLoading = false,
                                        viewState = ViewState.SuccessFirstInitMovieByGenre
                                    )
                                }
                            }
                        }
                    }
                    is Result.Error -> {
                        if (isLoadMore) {
                            setState {
                                copy(
                                    listMovieByGenre = emptyList(),
                                    showLoading = false,
                                    viewState = ViewState.ErrorLoadMoreMovieByGenre
                                )
                            }
                        } else {
                            setState {
                                copy(
                                    listMovieByGenre = emptyList(),
                                    showLoading = false,
                                    viewState = ViewState.ErrorFirstInitMovieByGenre
                                )
                            }
                        }
                    }
                        else -> {}
                    }
            }
        }
    }


    private fun saveToFavorite(movie: Movie) {
        val params = InsertToFavoriteUseCase.Params(movie)
        viewModelScope.launch {

            insertToFavoriteUseCase(params).collect{
                when(it) {
                    Result.SuccessNoReturn -> {
                        setState {
                            copy(
                                showLoading = false,
                            )
                        }
                    }
                    is Result.Error -> {
                        setState {
                            copy(
                                listFavorite = emptyList(),
                                showLoading = false,
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun removeFromFavorite(id: Int) {
        viewModelScope.launch {

            deleteFromFavoriteUseCase(id).collect{
                when(it) {
                    Result.SuccessNoReturn -> {
                        setState {
                            copy(
                                showLoading = false,
                            )
                        }
                    }
                    is Result.Error -> {
                        setState {
                            copy(
                                listFavorite = emptyList(),
                                showLoading = false,
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }


    private fun getFavorites() {
        viewModelScope.launch {

            getFavoritesUseCase().collect{
                when(it) {
                    Result.Loading -> {
                        setState {
                            copy(
                                showLoading = true,
                            )
                        }
                    }
                    is Result.Success -> {
                        if (it.data.isEmpty()) {
                            setState {
                                copy(
                                    listFavorite = emptyList(),
                                    showLoading = false,
                                    viewState = ViewState.EmptyListFirstInit
                                )
                            }
                        } else {
                            setState {
                                copy(
                                    listFavorite = it.data,
                                    showLoading = false,
                                    viewState = ViewState.SuccessFirstInit
                                )
                            }
                        }
                    }
                    is Result.Error -> {
                        setState {
                            copy(
                                listFavorite = emptyList(),
                                showLoading = false,
                                viewState = ViewState.ErrorFirstInit
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}
