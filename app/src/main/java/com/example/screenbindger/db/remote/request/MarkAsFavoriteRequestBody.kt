package com.example.screenbindger.db.remote.request

import com.google.gson.annotations.SerializedName

data class MarkAsFavoriteRequestBody constructor(
    @SerializedName("media_type") val mediaType: String = "movie",
    @SerializedName("media_id") val mediaId: Int,
    @SerializedName("favorite") val favorite: Boolean
)