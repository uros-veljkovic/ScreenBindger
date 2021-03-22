package com.example.screenbindger.view.fragment

sealed class ShowListViewAction
object FetchMovies : ShowListViewAction()
object FetchTvShows : ShowListViewAction()
object GotoNextPage : ShowListViewAction()
object GotoPreviousPage : ShowListViewAction()
object ResetState: ShowListViewAction()
