package com.example.screenbindger.view.fragment.details

import com.example.screenbindger.model.domain.Item
import com.example.screenbindger.model.domain.movie.ShowEntity

sealed class ShowViewState {
    object Fetching : ShowViewState()
    object NotFetched : ShowViewState()
    data class Fetched(val show: ShowEntity) : ShowViewState()

    fun getData(): ShowEntity? = when (this) {
        is Fetched -> show
        else -> null
    }
}