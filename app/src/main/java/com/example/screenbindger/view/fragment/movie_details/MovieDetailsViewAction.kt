package com.example.screenbindger.view.fragment.movie_details

sealed class MovieDetailsViewAction {
    data class MarkAsFavorite(val movieID: Int) : MovieDetailsViewAction()
    data class MarkAsNotFavorite(val movieID: Int) : MovieDetailsViewAction()
}

