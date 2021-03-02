package com.example.screenbindger.view.fragment.favorite_movies

import com.example.screenbindger.model.domain.movie.ShowEntity

sealed class FavoritesViewState {
    data class ListLoaded(val list: List<ShowEntity>) : FavoritesViewState()
    object EmptyList : FavoritesViewState()
}