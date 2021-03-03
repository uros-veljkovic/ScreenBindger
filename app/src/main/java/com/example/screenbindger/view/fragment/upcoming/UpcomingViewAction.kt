package com.example.screenbindger.view.fragment.upcoming

sealed class UpcomingViewAction {
    object FetchMovies : UpcomingViewAction()
    object FetchTvShows : UpcomingViewAction()
}