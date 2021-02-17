package com.example.screenbindger.util.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.screenbindger.R
import com.example.screenbindger.databinding.ItemReviewBinding
import com.example.screenbindger.model.domain.review.ReviewEntity

class ReviewRecyclerViewAdapter(
    private val list: List<ReviewEntity>
) : RecyclerView.Adapter<ReviewRecyclerViewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReviewBinding.inflate(inflater)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = list[position]

        holder.bind(review)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ReviewViewHolder constructor(val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: ReviewEntity) {
            binding.review = review
            fetchAvatar(review.authorDetails?.avatarPath)
        }

        private fun fetchAvatar(avatarPath: String?) {
            val url = "https://image.tmdb.org/t/p/w500$avatarPath"
            with(binding.ivAvatar) {
                Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_profile_outlined_black_24)
                    .into(this)
            }
        }
    }
}