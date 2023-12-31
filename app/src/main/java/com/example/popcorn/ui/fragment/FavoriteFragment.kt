package com.example.popcorn.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.popcorn.R

import com.example.popcorn.base.BaseFragment
import com.example.popcorn.databinding.FragmentFavoriteBinding
import com.example.popcorn.domain.model.Movie
import com.example.popcorn.ui.adapter.FavoriteMovieListener
import com.example.popcorn.ui.adapter.MovieAdapter
import com.example.popcorn.util.Intent
import com.example.popcorn.util.State
import com.example.popcorn.util.ViewState

class FavoriteFragment :
    BaseFragment<Intent,
            State>(),
    FavoriteMovieListener
    {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) {
            invalidate(it)
        }

        setupRecyclerView()
        viewModel.onIntentReceived(Intent.GetFavorites)
    }

    private fun setupRecyclerView() {
        with(binding) {
            recyclerview.setHasFixedSize(true)
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            recyclerview.layoutManager = gridLayoutManager
            recyclerview.itemAnimator = null
            adapter = MovieAdapter(this@FavoriteFragment, this@FavoriteFragment)
            adapter.setHasStableIds(true)
            recyclerview.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override val layoutResourceId: Int
        get() = R.layout.fragment_favorite

    override fun invalidate(state: State) {
        with(binding) {
            pgProgressList.visibility = if (state.showLoading) View.VISIBLE else View.GONE
        }

        when (state.viewState) {
            ViewState.EmptyListFirstInit -> {
                with(binding) {
                    errorView.visibility = View.VISIBLE
                    errorView.run {
                        setUpErrorView(
                            title = resources.getString(R.string.empty_state_title),
                            message = resources.getString(R.string.empty_state_message)
                        )
                        binding.buttonRetry.setOnClickListener {
                            viewModel.onIntentReceived(Intent.GetFavorites)
                        }
                    }
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.ErrorFirstInit -> {
                with(binding) {
                    errorView.visibility = View.VISIBLE
                    errorView.run {
                        setUpErrorView()
                        binding.buttonRetry.setOnClickListener {
                            viewModel.onIntentReceived(Intent.GetFavorites)
                        }
                    }
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.SuccessFirstInit -> {
                with(binding) {
                    recyclerview.visibility = View.VISIBLE
                    adapter.setData(state.listFavorite)
                    errorView.visibility = View.GONE
                }
            }

            else -> {}
        }
    }

    override fun insetFavoriteMovie(movie: Movie) {
        viewModel.onIntentReceived(Intent.SaveToFavorite(movie))
    }

    override fun deleteFavoriteMovie(id: Int) {
        viewModel.onIntentReceived(Intent.RemoveFromFavorite(id))
    }
    }
