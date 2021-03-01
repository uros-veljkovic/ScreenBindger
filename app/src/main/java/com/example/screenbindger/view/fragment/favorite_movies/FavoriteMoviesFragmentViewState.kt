package com.example.screenbindger.view.fragment.favorite_movies

import com.example.screenbindger.model.domain.movie.ShowEntity

sealed class FavoriteMoviesFragmentViewState {
    data class MoviesLoaded(val list: List<ShowEntity>) : FavoriteMoviesFragmentViewState()
    object EmptyList : FavoriteMoviesFragmentViewState()
}