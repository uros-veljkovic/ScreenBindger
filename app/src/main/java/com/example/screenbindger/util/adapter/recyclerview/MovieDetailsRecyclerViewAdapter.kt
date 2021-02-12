package com.example.screenbindger.util.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.screenbindger.databinding.ItemMovieCastsBinding
import com.example.screenbindger.databinding.ItemMovieDetailsBinding
import com.example.screenbindger.model.domain.CastEntity
import com.example.screenbindger.model.domain.Item
import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.model.enums.ItemType
import java.lang.ref.WeakReference


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
                with(holder as MovieDetailsViewHolder) {
                    bind(item)

                    val movie = item as MovieEntity
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

    /**
     * Adapter notifies that data set is changed only when
     * both types of items are in the list.
     * 
     * For example, if either movie object or cast list is in the adapter list,
     * when another object (cast list/movie) is inserted to list, only then
     * adapter notifies change.
     */
    fun addItems(items: List<Item>) {
        val itemTypeOfFirstItem = items[0].getItemType()
        val listContainedItem: Boolean = this.list.isEmpty().not()

        when (itemTypeOfFirstItem) {
            ItemType.MOVIE_DETAILS -> {
                val movie = items[0] as MovieEntity
                movie.generateGenreString()
                list.add(0, movie)
            }
            ItemType.CAST -> {
                list.addAll(items)
            }
        }

        if (listContainedItem) {
            notifyDataSetChanged()
        }
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