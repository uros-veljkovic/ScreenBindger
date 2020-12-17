package com.example.screenbindger.util.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.screenbindger.databinding.ItemGenreBinding
import com.example.screenbindger.databinding.ItemMovieBinding
import com.example.screenbindger.model.domain.GenreEntity
import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.util.adapter.recyclerview.listener.OnCardItemClickListener
import com.example.screenbindger.util.constants.API_IMAGE_BASE_URL
import com.example.screenbindger.util.constants.API_KEY
import com.example.screenbindger.util.constants.POSTER_SIZE_SMALL
import kotlinx.android.synthetic.main.item_movie.view.*

class ItemGenreRecyclerViewAdapter(
    val listener: OnCardItemClickListener,
    private var list: MutableList<GenreEntity> = mutableListOf()
) :
    RecyclerView.Adapter<ItemGenreRecyclerViewAdapter.ItemGenreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemGenreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGenreBinding.inflate(inflater)
        return ItemGenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemGenreViewHolder, position: Int) {
        val genre = list[position]

        holder.bind(genre)

        holder.itemView.setOnClickListener { listener.onCardItemClick(position) }
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

    fun setList(list: List<GenreEntity>?) {
        this.list.clear()
        this.list = list?.toMutableList() ?: mutableListOf()
        notifyDataSetChanged()
    }

    fun getList(): List<GenreEntity> {
        return list
    }

    inner class ItemGenreViewHolder constructor(val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: GenreEntity) {
            binding.genre = genre
        }
    }
}