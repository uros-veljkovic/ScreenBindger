package com.example.screenbindger.view.fragment.upcoming

sealed class UpcomingFragmentViewAction {
    object FetchMovies : UpcomingFragmentViewAction()
    object FetchTvShows : UpcomingFragmentViewAction()
}