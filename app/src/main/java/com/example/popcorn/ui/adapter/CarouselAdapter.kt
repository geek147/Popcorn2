package com.example.popcorn.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.popcorn.R
import com.example.popcorn.data.BuildConfig
import com.example.popcorn.databinding.ItemCarouselBinding
import com.example.popcorn.domain.model.Movie
import com.example.popcorn.ui.fragment.DetailFragment
import com.example.popcorn.ui.fragment.ListMovieFragment

class CarouselAdapter(private var fragment: Fragment) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {
    private var listMovies: MutableList<Movie> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val itemBinding = ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return if (listMovies.isEmpty()) {
            0
        } else {
            listMovies.size
        }
    }

    fun setData(list: List<Movie>) {
        this.listMovies.clear()
        this.listMovies.addAll(list)
        notifyDataSetChanged()
    }

    fun addData(list: List<Movie>) {
        this.listMovies.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        listMovies[holder.bindingAdapterPosition].let {
            holder.bindData(it, fragment)
        }
    }

    class CarouselViewHolder(private val binding: ItemCarouselBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(model: Movie, fragment: Fragment) {

            Glide
                .with(fragment)
                .load(BuildConfig.BACKDROP_URL + model?.backdropPath)
                .centerCrop()
                .placeholder(R.drawable.ic_not_found)
                .into(binding.imageView)

            val bundle = Bundle()
            bundle.putParcelable(DetailFragment.EXTRA_USER_DETAIL, model)
            itemView.setOnClickListener {
                fragment.findNavController().navigate(R.id.action_listMovieFragment_to_detailFragment, bundle)
            }
        }
    }
}
