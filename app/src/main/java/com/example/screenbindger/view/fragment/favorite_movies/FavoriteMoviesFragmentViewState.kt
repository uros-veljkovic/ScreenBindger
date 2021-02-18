package com.example.screenbindger.view.fragment.favorite_movies

import com.example.screenbindger.model.domain.movie.MovieEntity

sealed class FavoriteMoviesFragmentViewState {
    data class MoviesLoaded(val list: List<MovieEntity>) : FavoriteMoviesFragmentViewState()
    object EmptyList : FavoriteMoviesFragmentViewState()
}