package com.example.screenbindger.db.remote.response

import com.example.screenbindger.model.domain.MovieEntity

data class FavoriteMovieListResponse(
    val list: List<MovieEntity>? = emptyList()
)