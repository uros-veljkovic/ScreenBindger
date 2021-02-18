package com.example.screenbindger.util.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.screenbindger.databinding.ItemMovieBigBinding
import com.example.screenbindger.model.domain.movie.MovieEntity
import com.example.screenbindger.util.constants.API_IMAGE_BASE_URL
import com.example.screenbindger.util.constants.API_KEY
import com.example.screenbindger.util.constants.POSTER_SIZE_ORIGINAL
import com.example.screenbindger.util.extensions.setProgress
import com.example.screenbindger.util.extensions.setProgressBarColor
import com.example.screenbindger.view.fragment.favorite_movies.OnFavoriteItemClickListener
import kotlinx.android.synthetic.main.item_movie_big.view.*
import java.lang.ref.WeakReference

class BigItemMovieRecyclerViewAdapter(
    val listener: WeakReference<OnFavoriteItemClickListener>,
    private var list: List<MovieEntity> = mutableListOf()
) :
    RecyclerView.Adapter<BigItemMovieRecyclerViewAdapter.BigItemMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BigItemMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBigBinding.inflate(inflater, parent, false)
        return BigItemMovieViewHolder(listener, binding)
    }

    override fun onBindViewHolder(holder: BigItemMovieViewHolder, position: Int) {
        val movie = list[position]

        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<MovieEntity>?) {
        this.list = list ?: emptyList()
        notifyDataSetChanged()
    }

    inner class BigItemMovieViewHolder constructor(
        val listener: WeakReference<OnFavoriteItemClickListener>,
        val binding: ItemMovieBigBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity) {
            binding.movie = movie
            setRating(movie)
            setOnClickListeners(movie)
            bindPoster(binding.ivMoviePoster, movie.smallPosterUrl)
        }

        private fun setRating(movie: MovieEntity) {
            val rating = movie.rating?.times(10)?.toInt() ?: 0
            binding.pbRating.progress = rating
        }

        private fun bindPoster(imageView: ImageView, posterUrl: String?) {
            val field = "$API_IMAGE_BASE_URL/t/p/$POSTER_SIZE_ORIGINAL${posterUrl}?api_key=$API_KEY"

            Glide.with(imageView.context)
                .load(field)
                .centerCrop()
                .placeholder(null)
                .into(imageView)

            imageView.refreshDrawableState()
        }

        private fun setOnClickListeners(
            movie: MovieEntity
        ) {
            binding.also {
                it.container.setOnClickListener {
                    listener.get()?.onContainerClick(movie.id!!)
                }
                it.btnGotoComments.setOnClickListener {
                    listener.get()?.onCommentsButtonClick(movie.id!!)
                }
            }
        }
    }


}