package com.example.screenbindger.util.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.screenbindger.R
import com.example.screenbindger.util.constants.API_IMAGE_BASE_URL
import com.example.screenbindger.util.constants.API_KEY

@BindingAdapter(value = ["posterUrl", "posterSize"], requireAll = true)
fun ImageView.setLoadFrom(url: String?, size: String) {
    val field = "$API_IMAGE_BASE_URL/t/p/${size}${url}?api_key=$API_KEY"

    Glide.with(context)
        .load(field)
        .placeholder(R.drawable.ic_fire_black_24)
        .into(this)

    refreshDrawableState()
}