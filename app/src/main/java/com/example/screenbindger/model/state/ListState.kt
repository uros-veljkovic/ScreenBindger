package com.example.screenbindger.model.state

import com.example.screenbindger.util.event.Event

sealed class ListState {
    object Fetching : ListState()
    sealed class Fetched() : ListState() {
        object Movies : Fetched()
        object TvShows : Fetched()
    }

    data class NotFetched(val message: Event<String>) : ListState()
}