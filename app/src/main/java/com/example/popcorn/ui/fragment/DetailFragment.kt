package com.example.popcorn.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.popcorn.R
import com.example.popcorn.base.BaseFragment
import com.example.popcorn.data.BuildConfig
import com.example.popcorn.databinding.FragmentDetailBinding
import com.example.popcorn.domain.model.Movie
import com.example.popcorn.ui.adapter.UserReviewAdapter
import com.example.popcorn.util.Intent
import com.example.popcorn.util.State
import com.example.popcorn.util.ViewState


class DetailFragment :
    BaseFragment<Intent,
            State>(){
    companion object {
        val TAG = this::class.simpleName
        const val EXTRA_USER_DETAIL = "EXTRA_USER_DETAIL:"

        @JvmStatic
        fun create(
            movie: Movie
        ): DetailFragment {
            val fragment = DetailFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(EXTRA_USER_DETAIL, movie)
            }

            return fragment
        }
    }


    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    var movie: Movie? = null

    private lateinit var adapter: UserReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        movie = arguments?.getParcelable(EXTRA_USER_DETAIL)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.state.observe(viewLifecycleOwner) {
            invalidate(it)
        }


        setupRecyclerView()
        setupUIData()
        viewModel.onIntentReceived(Intent.GetUserReview(movie?.id?:12))
    }

    private fun setupRecyclerView() {
        with(binding) {
            recyclerview.setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(requireContext())
            recyclerview.layoutManager = linearLayoutManager
            recyclerview.itemAnimator = null
            adapter = UserReviewAdapter(requireContext())
            adapter.setHasStableIds(true)
            recyclerview.adapter = adapter
        }
    }

    private fun setupUIData () {

        activity?.title = movie?.title.orEmpty()

        with(binding) {
            tvTitle.text = movie?.title.orEmpty()
            tvOverview.text = movie?.overview.orEmpty()
            tvReleaseDate.text = movie?.releaseDate.orEmpty()

            Glide
                .with(this@DetailFragment)
                .load(BuildConfig.IMAGE_URL + movie?.posterPath)
                .centerCrop()
                .placeholder(R.drawable.ic_not_found)
                .into(ivPoster);

            Glide
                .with(this@DetailFragment)
                .load(BuildConfig.BACKDROP_URL + movie?.backdropPath)
                .centerCrop()
                .placeholder(R.drawable.ic_not_found)
                .into(ivBackdrop);
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override val layoutResourceId: Int
        get() = R.layout.fragment_detail

    override fun invalidate(state: State) {
        with(binding) {
            pgProgressList.visibility = if (state.showLoading) View.VISIBLE else View.GONE
        }

        when (state.viewState) {
            ViewState.EmptyListFirstInitUserReview -> {
                with(binding) {
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.ErrorFirstInitUserReview -> {
                with(binding) {
                    adapter.setData(emptyList())
                    recyclerview.visibility = View.GONE
                }
            }
            ViewState.SuccessFirstInitUserReview -> {
                with(binding) {
                    recyclerview.visibility = View.VISIBLE
                    adapter.setData(state.listUserReview)
                }
            }

            ViewState.SuccessLoadMoreUserReview -> {
                with(binding) {
                    recyclerview.visibility = View.VISIBLE
                    adapter.addData(state.listUserReview)
                }
            }
            ViewState.EmptyListLoadMoreUserReview-> {
                Toast.makeText(requireContext(), "new list is empty", Toast.LENGTH_SHORT).show()
            }
            ViewState.ErrorLoadMoreUserReview -> {
                with(binding) {
                    recyclerview.visibility = View.VISIBLE
                }
            }

            else -> {}
        }
    }

}
