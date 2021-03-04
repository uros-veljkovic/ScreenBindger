package com.example.screenbindger.db.remote.response.tv_show

import com.example.screenbindger.model.domain.movie.ShowEntity
import com.google.gson.annotations.SerializedName

data class UpcomingTvShowResponse(
    @SerializedName("page")
    val page: Int,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("results")
    val list: List<ShowEntity>? = null
)