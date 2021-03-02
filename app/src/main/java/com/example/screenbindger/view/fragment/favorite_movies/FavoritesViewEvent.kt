package com.example.screenbindger.view.fragment.favorite_movies

import com.example.screenbindger.model.domain.movie.ShowEntity

sealed class FavoritesViewEvent {
    data class MoviesLoaded(val list: List<ShowEntity>) : FavoritesViewEvent()
    data class EmptyList(val message: String) : FavoritesViewEvent()
    data class Error(val message: String) : FavoritesViewEvent()
}