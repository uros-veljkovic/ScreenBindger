package com.example.screenbindger.view.fragment.details

sealed class DetailsFragmentViewAction {
    object FetchTrailers : DetailsFragmentViewAction()
    data class MarkAsFavorite(val id: Int) : DetailsFragmentViewAction()
    data class MarkAsNotFavorite(val id: Int) : DetailsFragmentViewAction()
}

