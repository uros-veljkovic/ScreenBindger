package com.example.screenbindger.util.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.screenbindger.R
import com.example.screenbindger.app.ScreenBindger
import com.example.screenbindger.databinding.ItemGenreBinding
import com.example.screenbindger.model.domain.GenreEntity
import com.example.screenbindger.util.adapter.recyclerview.listener.OnCardItemClickListener
import java.lang.ref.WeakReference

class ItemGenreRecyclerViewAdapter(
    val context: WeakReference<Context>,
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
        setGenreImage(genre, holder)

        holder.itemView.setOnClickListener { listener.onCardItemClick(position) }
    }

    private fun setGenreImage(genre: GenreEntity, holder: ItemGenreViewHolder) {
        val resources = context.get()?.resources

        val resourceId: Int = resources!!.getIdentifier(
            "ic_genre_${genre.id}",
            "drawable",
            context.get()?.packageName
        )

        val drawable = try {
            ContextCompat.getDrawable(context.get()!!, resourceId)
        } catch (e: Exception) {
            ContextCompat.getDrawable(context.get()!!, R.drawable.ic_image_frame_black_24)!!
        }

        holder.binding.ivGenreImage.let {
            it.setImageDrawable(drawable)
            it.refreshDrawableState()
        }
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