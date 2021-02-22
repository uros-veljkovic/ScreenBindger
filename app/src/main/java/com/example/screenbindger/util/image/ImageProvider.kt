package com.example.screenbindger.util.image

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ImageProvider {

    suspend fun download(uri: Uri, context: Context): Bitmap? = withContext(Dispatchers.IO) {
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .submit()
            .get()
    }
}