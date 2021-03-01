package com.example.screenbindger.db.remote.response.tv_show
import com.example.screenbindger.model.domain.movie.ShowEntity
import com.google.gson.annotations.SerializedName

data class UpcomingTvShowResponse(
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("results")
    val list: List<ShowEntity>? = null
)