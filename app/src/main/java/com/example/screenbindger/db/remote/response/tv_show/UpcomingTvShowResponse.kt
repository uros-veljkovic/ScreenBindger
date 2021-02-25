package com.example.screenbindger.db.remote.response.tv_show
import com.example.screenbindger.model.domain.movie.MovieEntity
import com.google.gson.annotations.SerializedName

data class UpcomingTvShowResponse(
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("results")
    val list: List<MovieEntity>? = null
)