package com.example.screenbindger.view.fragment.favorite_movies

import com.example.screenbindger.model.domain.movie.MovieEntity

sealed class FavoriteMoviesFragmentViewEvent {
    data class MoviesLoaded(val list: List<MovieEntity>) : FavoriteMoviesFragmentViewEvent()
    data class EmptyList(val message: String) : FavoriteMoviesFragmentViewEvent()
    data class Error(val message: String) : FavoriteMoviesFragmentViewEvent()
}