package com.example.screenbindger.util.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.screenbindger.databinding.ItemMovieCastsBinding
import com.example.screenbindger.databinding.ItemMovieDetailsBinding
import com.example.screenbindger.model.domain.cast.CastEntity
import com.example.screenbindger.model.domain.Item
import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.model.enums.ItemType


class MovieDetailsRecyclerViewAdapter(
    val listener: OnClickListener,
    val list: MutableList<Item> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var containsMovieAndCast = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var viewHolder: RecyclerView.ViewHolder? = null

        when (viewType) {
            ItemType.MOVIE_DETAILS.value -> {
                val binding = ItemMovieDetailsBinding.inflate(inflater, parent, false)
                viewHolder = MovieDetailsViewHolder(binding)
            }
            ItemType.CAST.value -> {
                val binding = ItemMovieCastsBinding.inflate(inflater, parent, false)
                viewHolder = MovieCastsViewHolder(binding)

            }
        }

        return viewHolder!!
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        when (item.getItemType()) {
            ItemType.MOVIE_DETAILS -> {
                with(holder as MovieDetailsViewHolder) {
                    holder.binding.apply {
                        btnWatchTrailer.setOnClickListener {
                            listener.onBtnWatchTrailer()
                        }
                        fabInstagram.setOnClickListener {
                            val movie = item as ShowEntity
                            listener.onBtnShareToInstagram(movie)
                        }
                    }
                    bind(item)
                }

            }
            ItemType.CAST -> {
                with(holder as MovieCastsViewHolder) {
                    bind(item)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val itemType = list[position].getItemType()
        return itemType.value
    }

    fun addItems(items: List<Item>) {
        this.list.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    inner class MovieDetailsViewHolder constructor(val binding: ItemMovieDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.movie = item as ShowEntity?
        }
    }

    inner class MovieCastsViewHolder constructor(val binding: ItemMovieCastsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.cast = item as CastEntity?
        }
    }

    interface OnClickListener {
        fun onBtnWatchTrailer()
        fun onBtnShareToInstagram(movieEntity: ShowEntity)
    }

}