package com.example.screenbindger.view.fragment.trending

sealed class TrendingFragmentViewAction {
    object FetchMovies : TrendingFragmentViewAction()
    object FetchTvShows : TrendingFragmentViewAction()
}