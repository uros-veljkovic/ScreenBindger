package com.example.screenbindger.view.fragment.movie_details

sealed class MovieDetailsFragmentViewAction {
    object FetchTrailers : MovieDetailsFragmentViewAction()
    data class MarkAsFavorite(val movieID: Int) : MovieDetailsFragmentViewAction()
    data class MarkAsNotFavorite(val movieID: Int) : MovieDetailsFragmentViewAction()
}

