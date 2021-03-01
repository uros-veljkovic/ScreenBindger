package com.example.screenbindger.view.fragment.favorite_movies

import com.example.screenbindger.model.domain.movie.ShowEntity

sealed class FavoriteMoviesFragmentViewEvent {
    data class MoviesLoaded(val list: List<ShowEntity>) : FavoriteMoviesFragmentViewEvent()
    data class EmptyList(val message: String) : FavoriteMoviesFragmentViewEvent()
    data class Error(val message: String) : FavoriteMoviesFragmentViewEvent()
}