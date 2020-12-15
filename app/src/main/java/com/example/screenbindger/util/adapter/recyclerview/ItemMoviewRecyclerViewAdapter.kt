package com.example.screenbindger.util.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.screenbindger.databinding.ItemMovieBinding
import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.util.constants.API_IMAGE_BASE_URL
import com.example.screenbindger.util.constants.API_KEY
import com.example.screenbindger.util.constants.POSTER_SIZE_SMALL
import kotlinx.android.synthetic.main.item_movie.view.*

class ItemMoviewRecyclerViewAdapter(
    val listener: OnMovieItemClickListener,
    private var list: MutableList<MovieEntity> = mutableListOf()
) :
    RecyclerView.Adapter<ItemMoviewRecyclerViewAdapter.ItemMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(inflater)
        return ItemMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemMovieViewHolder, position: Int) {
        val movie = list[position]
        val poster = holder.itemView.ivMoviePoster

        holder.bind(movie)
        bindPoster(poster, movie.smallPosterUrl)

        holder.itemView.setOnClickListener { listener.onOfferItemClick(position) }
    }

    private fun bindPoster(imageView: ImageView, smallPosterUrl: String?) {
        val field = "$API_IMAGE_BASE_URL/t/p/$POSTER_SIZE_SMALL${smallPosterUrl}?api_key=$API_KEY"

        Glide.with(imageView.context)
            .load(field)
            .centerCrop()
            .placeholder(null)
            .into(imageView)

        imageView.refreshDrawableState()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<MovieEntity>?) {
        this.list.clear()
        this.list = list?.toMutableList() ?: mutableListOf()
        notifyDataSetChanged()
    }

    fun getList(): List<MovieEntity> {
        return list
    }

    inner class ItemMovieViewHolder constructor(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity) {
            binding.movie = movie
        }
    }

    interface OnMovieItemClickListener {
        fun onOfferItemClick(position: Int)
    }
}