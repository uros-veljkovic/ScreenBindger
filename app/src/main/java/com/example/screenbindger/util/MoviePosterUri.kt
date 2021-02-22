package com.example.screenbindger.util

import android.net.Uri
import com.example.screenbindger.util.constants.API_IMAGE_BASE_URL
import com.example.screenbindger.util.constants.API_KEY
import com.example.screenbindger.util.constants.POSTER_SIZE_ORIGINAL

object MoviePosterUri {

    object Builder {

        private var imageBaseUrl = "$API_IMAGE_BASE_URL/t/p/"
        private var imageSize: String = POSTER_SIZE_ORIGINAL
        private lateinit var imagePath: String
        private const val pathApiKey = "?api_key=$API_KEY"
        private lateinit var uri: Uri

        fun withPosterPath(path: String): Builder {
            imagePath = path
            return this
        }

        fun withPosterSize(posterSize: String): Builder {
            imageSize = posterSize
            return this
        }

        fun build(): Uri {
            val uri = imageBaseUrl + imageSize + imagePath + pathApiKey
            return Uri.parse(uri)
        }
    }

}