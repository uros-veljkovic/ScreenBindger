package com.example.screenbindger.view.fragment.trending

sealed class TrendingViewAction {
    object FetchMovies : TrendingViewAction()
    object FetchTvShows : TrendingViewAction()
    object GotoNextPage : TrendingViewAction()
    object GotoPreviousPage : TrendingViewAction()
}