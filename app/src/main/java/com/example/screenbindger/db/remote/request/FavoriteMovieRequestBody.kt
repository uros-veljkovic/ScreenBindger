package com.example.screenbindger.db.remote.request

import com.google.gson.annotations.SerializedName

data class FavoriteMovieRequestBody constructor(
    @SerializedName("media_type") val mediaType: String,
    @SerializedName("media_id") val mediaId: String,
    @SerializedName("favorite") val favorite: Boolean
)