package com.example.popcorn.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.popcorn.databinding.ListItemRowBinding
import com.example.popcorn.databinding.ListItemUserReviewRowBinding
import com.example.popcorn.domain.model.UserReview

class UserReviewAdapter(private var context: Context) : RecyclerView.Adapter<UserReviewAdapter.MainViewHolder>() {
    private var listUserReview: MutableList<UserReview> = mutableListOf()
    private val guidToIdMap = GuidToIdMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val listItemUserReviewRowBinding = ListItemUserReviewRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(listItemUserReviewRowBinding)
    }

    override fun getItemCount(): Int {
        return if (listUserReview.isNullOrEmpty()) {
            0
        } else {
            listUserReview.size
        }
    }

    override fun getItemId(position: Int): Long {
        val userReview: UserReview = listUserReview[position]
        return guidToIdMap.getIdByGuid(userReview.id)
    }

    fun addData(list: List<UserReview>) {
        this.listUserReview.addAll(list)
        notifyDataSetChanged()
    }

    fun setData(list: List<UserReview>) {
        this.listUserReview.clear()
        this.listUserReview.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        listUserReview[holder.bindingAdapterPosition].let {
            holder.bindData(it, context)
        }
    }

    class MainViewHolder(private val binding: ListItemUserReviewRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(model: UserReview, context: Context) {
            with(binding) {
                textAuthor.text = model.author
                textCreatedAt.text = model.createdAt
                textContent.text = model.content
            }
        }
    }

    /*
    Generate unique id
     */
    class GuidToIdMap {
        private val guidToIdMapping: MutableMap<String, Long> = HashMap()
        private val idToGuidMapping: MutableMap<Long, String> = HashMap()
        private var idGenerator: Long = 0
        fun getIdByGuid(guid: String): Long {
            if (!guidToIdMapping.containsKey(guid)) {
                val id = idGenerator++
                guidToIdMapping[guid] = id
                idToGuidMapping[id] = guid
            }
            return guidToIdMapping[guid]!!
        }

        fun getGuidById(id: Long): String? {
            return if (idToGuidMapping.containsKey(id)) {
                idToGuidMapping[id]
            } else null
        }
    }
}
