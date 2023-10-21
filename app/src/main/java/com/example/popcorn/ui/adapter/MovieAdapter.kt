package com.example.popcorn.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.popcorn.R
import com.example.popcorn.data.BuildConfig
import com.example.popcorn.databinding.ListItemRowBinding
import com.example.popcorn.domain.model.Movie
import com.example.popcorn.ui.fragment.DetailFragment
import com.example.popcorn.ui.fragment.ListMovieFragment

class MovieAdapter(private var fragment: Fragment, private var favoriteMovieListener: FavoriteMovieListener) : RecyclerView.Adapter<MovieAdapter.MainViewHolder>() {
    private var listMovies: MutableList<Movie> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val listRowBinding = ListItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(listRowBinding)
    }

    override fun getItemCount(): Int {
        return if (listMovies.isNullOrEmpty()) {
            0
        } else {
            listMovies.size
        }
    }

    override fun getItemId(position: Int): Long {
        val movie: Movie = listMovies[position]
        return movie.id.toLong()
    }

    fun addData(list: List<Movie>) {
        this.listMovies.addAll(list)
        notifyDataSetChanged()
    }

    fun setData(list: List<Movie>) {
        this.listMovies.clear()
        this.listMovies.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        listMovies[holder.bindingAdapterPosition].let {
            holder.bindData(it, fragment, favoriteMovieListener)
        }
    }

    class MainViewHolder(private val binding: ListItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(model: Movie, fragment: Fragment, favoriteMovieListener: FavoriteMovieListener) {
            with(binding) {
                Glide
                    .with(fragment)
                    .load(BuildConfig.IMAGE_URL + model.posterPath)
                    .centerCrop()
                    .placeholder(R.drawable.ic_not_found)
                    .into(binding.ivMoviePoster)
                tvMovieTitle.text = model.title
                tgFavorite.visibility = if (model.isPopularMovie) View.VISIBLE else View.GONE
                tgFavorite.isChecked= model.isLiked
                tgFavorite.background= if (model.isLiked) fragment.context?.let {
                    ContextCompat.getDrawable(
                        it, R.drawable.star_yellow)
                } else fragment.context?.let { ContextCompat.getDrawable(it, R.drawable.star_grey) }

                tgFavorite.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        binding.tgFavorite.setBackgroundResource(R.drawable.star_yellow)
                        model.isLiked = true
                        favoriteMovieListener.insetFavoriteMovie(model)
                    } else {
                        binding.tgFavorite.setBackgroundResource(R.drawable.star_grey)
                        model.isLiked = false
                        favoriteMovieListener.deleteFavoriteMovie(model.id)
                    }
                }
            }

            val bundle = Bundle()
            bundle.putParcelable(DetailFragment.EXTRA_USER_DETAIL, model)
            itemView.setOnClickListener {
                if (fragment is ListMovieFragment) {
                    fragment.findNavController().navigate(R.id.action_listMovieFragment_to_detailFragment, bundle)
                } else {
                    fragment.findNavController().navigate(R.id.action_favoriteFragment_to_detailFragment, bundle)
                }
            }
        }
    }
}

interface FavoriteMovieListener {
    fun insetFavoriteMovie(movie: Movie)
    fun deleteFavoriteMovie(id: Int)
}