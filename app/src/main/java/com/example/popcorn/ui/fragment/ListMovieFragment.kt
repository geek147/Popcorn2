package com.example.popcorn.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.popcorn.R

import com.example.popcorn.base.BaseFragment
import com.example.popcorn.databinding.FragmentListMovieBinding
import com.example.popcorn.domain.model.Movie
import com.example.popcorn.ui.adapter.CarouselAdapter
import com.example.popcorn.ui.adapter.FavoriteMovieListener
import com.example.popcorn.ui.adapter.MovieAdapter
import com.example.popcorn.util.EndlessRecyclerViewScrollListener
import com.example.popcorn.util.HorizontalMarginItemDecoration
import com.example.popcorn.util.Intent
import com.example.popcorn.util.State
import com.example.popcorn.util.ViewState

class ListMovieFragment :
    BaseFragment<Intent,
            State>(), FavoriteMovieListener {

    companion object {
        val TAG = this::class.simpleName
        const val EXTRA_USER_LIST_MOVIE = "EXTRA_USER_LIST_MOVIE"

        @JvmStatic
        fun create(
            withGenre: Int
        ): ListMovieFragment {
            val fragment = ListMovieFragment()
            fragment.arguments = Bundle().apply {
                putInt(EXTRA_USER_LIST_MOVIE, withGenre)
            }

            return fragment
        }
    }

    private var _binding: FragmentListMovieBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MovieAdapter
    private lateinit var carouselAdapter: CarouselAdapter

    var withGenre   : Int? = null

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListMovieBinding.inflate(inflater, container, false)
        withGenre = arguments?.getInt(EXTRA_USER_LIST_MOVIE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) {
            invalidate(it)
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                val navHostFragment = requireActivity().supportFragmentManager
                    .findFragmentById(R.id.mainNavFragment) as NavHostFragment
                val navController = navHostFragment.navController
                navController.navigate(R.id.action_listMovieFragment_to_favorite_fragment)
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setupRecyclerView()
        viewModel.onIntentReceived(Intent.GetMovieByGenre(withGenre = withGenre?:27))
    }

    private fun setupRecyclerView() {
        with(binding) {
            recyclerview.setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            recyclerview.layoutManager = linearLayoutManager
            recyclerview.itemAnimator = null
            adapter = MovieAdapter(this@ListMovieFragment, this@ListMovieFragment)
            adapter.setHasStableIds(true)
            recyclerview.adapter = adapter
            scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
                override fun onLoadMore(
                    page: Int,
                    totalItemsCount: Int,
                    view: RecyclerView?,
                ) {
                    currentPage = page + 1
                    viewModel.onIntentReceived(Intent.LoadNextMovieByGenre(currentPage, withGenre = withGenre?:27))
                }
            }
            recyclerview.addOnScrollListener(scrollListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override val layoutResourceId: Int
        get() = R.layout.fragment_list_movie

    override fun invalidate(state: State) {
        with(binding) {
            pgProgressList.visibility = if (state.showLoading) View.VISIBLE else View.GONE
        }

        when (state.viewState) {
            ViewState.EmptyListFirstInitMovieByGenre -> {
                with(binding) {
                    errorView.visibility = View.VISIBLE
                    errorView.run {
                        setUpErrorView(
                            title = resources.getString(R.string.empty_state_title),
                            message = resources.getString(R.string.empty_state_message)
                        )
                        binding.buttonRetry.setOnClickListener {
                            currentPage = 1
                            viewModel.onIntentReceived(Intent.GetMovieByGenre(withGenre = withGenre?:27))
                        }
                    }
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.ErrorFirstInitMovieByGenre -> {
                with(binding) {
                    errorView.visibility = View.VISIBLE
                    errorView.run {
                        setUpErrorView()
                        binding.buttonRetry.setOnClickListener {
                            currentPage = 1
                            viewModel.onIntentReceived(Intent.GetMovieByGenre(withGenre = withGenre?:27))
                        }
                    }
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.ErrorLoadMoreMovieByGenre -> {
                with(binding) {
                    recyclerview.visibility = View.VISIBLE
                }
            }
            ViewState.Idle -> {}
            ViewState.SuccessFirstInitMovieByGenre -> {
                with(binding) {
                    viewPager.visibility = View.VISIBLE
                    carouselAdapter = CarouselAdapter(this@ListMovieFragment)
                    carouselAdapter.setData(state.listMovieByGenre)
                    viewPager.adapter = carouselAdapter
                    setupCarousel(viewPager)

                    recyclerview.visibility = View.VISIBLE
                    adapter.setData(state.listMovieByGenre)
                    errorView.visibility = View.GONE
                }
            }
            ViewState.SuccessLoadMoreMovieByGenre -> {
                with(binding) {
                    carouselAdapter.addData(state.listMovieByGenre)

                    recyclerview.visibility = View.VISIBLE
                    adapter.addData(state.listMovieByGenre)
                    errorView.visibility = View.GONE
                }
            }
            ViewState.EmptyListLoadMoreMovieByGenre-> {
                Toast.makeText(requireContext(), "new list is empty", Toast.LENGTH_SHORT).show()
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

    private fun setupCarousel(viewPager : ViewPager2){

        viewPager.offscreenPageLimit = 1

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (0.25f * kotlin.math.abs(position))
            page.alpha = 0.25f + (1 - kotlin.math.abs(position))
        }
        viewPager.setPageTransformer(pageTransformer)
        val itemDecoration = this@ListMovieFragment.context?.let {
            HorizontalMarginItemDecoration(
                it,
                R.dimen.viewpager_current_item_horizontal_margin
            )
        }
        if (itemDecoration != null) {
            viewPager.addItemDecoration(itemDecoration)
        }
    }
}
