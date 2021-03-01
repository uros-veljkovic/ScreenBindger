package com.example.screenbindger.model.state

import com.example.screenbindger.util.event.Event

sealed class ListState {
    object Fetching : ListState()
    object Fetched : ListState()
    data class NotFetched(val message: Event<String>) : ListState()
}