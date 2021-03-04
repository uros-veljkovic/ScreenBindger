package com.example.screenbindger.view.fragment.upcoming

import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.view.ListViewState

sealed class UpcomingViewState {
    object Init: UpcomingViewState()
    object Fetching : UpcomingViewState()

    sealed class Fetched(
        val list: List<ShowEntity>,
        val currentPage: Int,
        val totalPages: Int
    ) : UpcomingViewState() {
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

    data class NotFetched(val message: Event<String>) : UpcomingViewState()
}