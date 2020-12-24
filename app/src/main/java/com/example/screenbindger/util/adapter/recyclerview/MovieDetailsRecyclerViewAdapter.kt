package com.example.screenbindger.util.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.screenbindger.R
import com.example.screenbindger.databinding.ItemMovieCastsBinding
import com.example.screenbindger.databinding.ItemMovieDetailsBinding
import com.example.screenbindger.model.domain.CastEntity
import com.example.screenbindger.model.domain.Item
import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.model.enums.ItemType
import com.example.screenbindger.util.constants.API_IMAGE_BASE_URL
import com.example.screenbindger.util.constants.API_KEY
import com.example.screenbindger.util.constants.POSTER_SIZE_ORIGINAL
import com.example.screenbindger.util.extensions.setLoadFrom
import kotlinx.android.synthetic.main.fragment_genre_movies.view.*
import kotlinx.android.synthetic.main.item_movie_details.view.*


class MovieDetailsRecyclerViewAdapter(
    val list: MutableList<Item> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
                (holder as MovieDetailsViewHolder).bind(item)
            }
            ItemType.CAST -> {
                (holder as MovieCastsViewHolder).bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val itemType = list[position].getItemType()
        return itemType.value
    }

    fun addItems(items: List<Item>) {
        val itemType = items[0].getItemType()
        when (itemType) {
            ItemType.MOVIE_DETAILS -> {
                val movie = items[0] as MovieEntity
                movie.generateGenreString()
                list.add(0, movie)
            }
            ItemType.CAST -> {
                list.addAll(items)
            }
        }
        notifyDataSetChanged()

    }

    inner class MovieDetailsViewHolder constructor(val binding: ItemMovieDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.movie = item as MovieEntity?
        }
    }

    inner class MovieCastsViewHolder constructor(val binding: ItemMovieCastsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.cast = item as CastEntity?
        }
    }

}