package com.example.screenbindger.db.remote.response.movie

import com.example.screenbindger.model.domain.movie.ShowEntity
import com.google.gson.annotations.SerializedName

class MoviesResponse
constructor(
    @SerializedName("page")
    val page: Int,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("results")
    val list: List<ShowEntity>
)