package com.example.screenbindger.view.fragment.favorite_movies

import com.example.screenbindger.model.domain.MovieEntity

sealed class FavoriteMoviesViewState {
    data class MoviesLoaded(val list: List<MovieEntity>) : FavoriteMoviesViewState()
    object EmptyList : FavoriteMoviesViewState()
}