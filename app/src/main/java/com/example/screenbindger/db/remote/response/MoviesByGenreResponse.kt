package com.example.screenbindger.db.remote.response

import com.example.screenbindger.model.domain.MovieEntity
import com.google.gson.annotations.SerializedName

class GenreMoviesResponse (
    @SerializedName("results")
    val list: List<MovieEntity>
)

