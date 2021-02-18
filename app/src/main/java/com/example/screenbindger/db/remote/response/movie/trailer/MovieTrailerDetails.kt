package com.example.screenbindger.db.remote.response.movie.trailer

import com.google.gson.annotations.SerializedName

data class MovieTrailerDetails(
    @SerializedName("id") val id: String? = null,
    @SerializedName("key") val key: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("site") val site: String? = null,
    @SerializedName("size") val size: Int? = null,
    @SerializedName("type") val type: String? = null
) {
}