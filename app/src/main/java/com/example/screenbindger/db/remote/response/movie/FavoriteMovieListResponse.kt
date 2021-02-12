package com.example.screenbindger.db.remote.response.movie

import com.example.screenbindger.model.domain.MovieEntity
import com.google.gson.annotations.SerializedName

data class FavoriteMovieListResponse(
    @SerializedName("results") val list: List<MovieEntity>? = emptyList()
)