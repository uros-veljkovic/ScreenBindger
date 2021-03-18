package com.example.screenbindger.util.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.screenbindger.databinding.ItemMovieSmallBinding
import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.util.adapter.recyclerview.listener.OnCardItemClickListener
import com.example.screenbindger.util.constants.API_IMAGE_BASE_URL
import com.example.screenbindger.util.constants.API_KEY
import com.example.screenbindger.util.constants.POSTER_SIZE_SMALL
import com.example.screenbindger.util.dialog.SortBy
import kotlinx.android.synthetic.main.item_movie_small.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmallItemMovieRecyclerViewAdapter(
    val listener: OnCardItemClickListener,
    private var list: MutableList<ShowEntity> = mutableListOf()
) :
    RecyclerView.Adapter<SmallItemMovieRecyclerViewAdapter.SmallItemMovieViewHolder>() {

    private lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView
    }

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

    fun setList(list: List<ShowEntity>?) {
        this.list.clear()
        this.list = list?.toMutableList() ?: mutableListOf()
        notifyDataSetChanged()
        recyclerView.startLayoutAnimation()
    }

    fun getList(): List<ShowEntity> {
        return list
    }

    fun sort(sortType: SortBy) {
        when (sortType) {
            SortBy.RatingAsc -> sortRatingAsc()
            SortBy.RatingDesc -> sortRatingDesc()
            SortBy.TitleAsc -> sortTitleAsc()
            SortBy.TitleDesc -> sortTitleDesc()
        }
    }

    private fun sortRatingAsc() {
        val sorted = list.sortedBy { it.rating }
        setListAndStartAnimation(sorted)
    }

    private fun sortRatingDesc() {
        val sorted = list.sortedByDescending { it.rating }
        setListAndStartAnimation(sorted)
    }

    private fun sortTitleAsc() {
        val sorted = list.sortedBy { it.title }
        setListAndStartAnimation(sorted)
    }

    private fun sortTitleDesc() {
        val sorted = list.sortedByDescending { it.title }
        setListAndStartAnimation(sorted)
    }

    private fun setListAndStartAnimation(sorted: List<ShowEntity>) {
        setList(sorted)
        recyclerView.startLayoutAnimation()
    }



    inner class SmallItemMovieViewHolder constructor(val binding: ItemMovieSmallBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: ShowEntity) {
            binding.movie = movie
        }
    }

}