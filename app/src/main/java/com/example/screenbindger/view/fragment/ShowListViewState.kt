package com.example.screenbindger.view.fragment

import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.util.event.Event

sealed class ShowListViewState {

    object Fetching : ShowListViewState()

    data class FetchedMovies(
        val list: List<ShowEntity>,
        val currentPage: Int,
        val totalPages: Int
    ) : ShowListViewState()

    data class FetchedTvShows(
        val list: List<ShowEntity>,
        val currentPage: Int,
        val totalPages: Int
    ) : ShowListViewState()

    data class NotFetched(val message: Event<String>) : ShowListViewState()
}