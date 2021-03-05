package com.example.screenbindger.view.fragment.trending

import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.view.ListViewState
import com.example.screenbindger.view.fragment.upcoming.UpcomingViewState

sealed class TrendingViewState {
    object Fetching : TrendingViewState()

    sealed class Fetched(
        val list: List<ShowEntity>,
        val currentPage: Int,
        val totalPages: Int
    ) : TrendingViewState() {
        class Movies(
            list: List<ShowEntity>,
            page: Int,
            totalPages: Int
        ) : Fetched(list, page, totalPages)

        class TvShows(
            list: List<ShowEntity>,
            page: Int,
            totalPages: Int
        ) : Fetched(list, page, totalPages)
    }

    data class NotFetched(val message: Event<String>) : TrendingViewState()
}
