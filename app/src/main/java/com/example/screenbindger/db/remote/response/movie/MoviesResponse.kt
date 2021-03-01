package com.example.screenbindger.db.remote.response.movie

import com.example.screenbindger.model.domain.movie.ShowEntity
import com.google.gson.annotations.SerializedName

class MoviesResponse
constructor(
    @SerializedName("results")
    val list: List<ShowEntity>
)