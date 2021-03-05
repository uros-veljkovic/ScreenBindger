package com.example.screenbindger.view.fragment.details

sealed class DetailsViewAction {
    object FetchTrailers : DetailsViewAction()
    object WatchTrailer : DetailsViewAction()

    data class MarkAsFavorite(val id: Int) : DetailsViewAction()
    data class MarkAsNotFavorite(val id: Int) : DetailsViewAction()
}

