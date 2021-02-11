package com.example.screenbindger.view.fragment.favorite_movies

import com.example.screenbindger.model.domain.MovieEntity

sealed class FavoriteMoviesViewEvent {
    data class MoviesLoaded(val list: List<MovieEntity>) : FavoriteMoviesViewEvent()
    data class EmptyList(val message: String) : FavoriteMoviesViewEvent()
    data class Error(val message: String) : FavoriteMoviesViewEvent()
}