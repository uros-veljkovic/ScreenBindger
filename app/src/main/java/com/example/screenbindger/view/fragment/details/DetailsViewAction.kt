package com.example.screenbindger.view.fragment.details

sealed class DetailsViewAction
object FetchTrailers : DetailsViewAction()
object AddOrRemoveFromFavorites : DetailsViewAction()


