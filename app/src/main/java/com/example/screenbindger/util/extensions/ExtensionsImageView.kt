package com.example.screenbindger.util.extensions

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.example.screenbindger.R
import com.example.screenbindger.util.constants.API_IMAGE_BASE_URL
import com.example.screenbindger.util.constants.API_KEY
import java.io.ByteArrayOutputStream

@BindingAdapter(value = ["posterUrl", "posterSize"], requireAll = true)
fun ImageView.setLoadFrom(url: String?, size: String) {
    val field = "$API_IMAGE_BASE_URL/t/p/${size}${url}?api_key=$API_KEY"

    Glide.with(context)
        .load(field)
        .placeholder(R.drawable.ic_fire_black_24)
        .into(this)

    refreshDrawableState()
}

@BindingAdapter("app:uri")
fun ImageView.setUri(uri: Uri?) {
    Glide.with(context)
        .load(uri)
        .signature(ObjectKey(System.currentTimeMillis().toString()))
        .placeholder(R.drawable.ic_profile_outlined_black_24)
        .into(this)
}

fun ImageView.setUri(uri: String) {
    Glide.with(context)
        .load(uri)
        .signature(ObjectKey(System.currentTimeMillis().toString()))
        .placeholder(R.drawable.ic_profile_outlined_black_24)
        .into(this)
}

fun ImageView.toByteArray(): ByteArray {
    val bitmap = drawable.toBitmap()
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    return baos.toByteArray()
}
