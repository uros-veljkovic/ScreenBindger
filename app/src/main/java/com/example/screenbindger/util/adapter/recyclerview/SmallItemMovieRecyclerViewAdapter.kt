package com.example.screenbindger.util.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.screenbindger.databinding.ItemMovieSmallBinding
import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.util.adapter.recyclerview.listener.OnCardItemClickListener
import com.example.screenbindger.util.constants.API_IMAGE_BASE_URL
import com.example.screenbindger.util.constants.API_KEY
import com.example.screenbindger.util.constants.POSTER_SIZE_SMALL
import kotlinx.android.synthetic.main.item_movie_small.view.*

class SmallItemMovieRecyclerViewAdapter(
    val listener: OnCardItemClickListener,
    private var list: MutableList<MovieEntity> = mutableListOf()
) :
    RecyclerView.Adapter<SmallItemMovieRecyclerViewAdapter.SmallItemMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallItemMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieSmallBinding.inflate(inflater)
        return SmallItemMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SmallItemMovieViewHolder, position: Int) {
        val movie = list[position]
        val poster = holder.itemView.ivMoviePoster

        holder.bind(movie)
        bindPoster(poster, movie.smallPosterUrl)

        holder.itemView.setOnClickListener { listener.onCardItemClick(movie.id!!) }
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

    inner class SmallItemMovieViewHolder constructor(val binding: ItemMovieSmallBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity) {
            binding.movie = movie
        }
    }

}