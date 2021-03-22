package com.example.screenbindger.view.fragment.details

import com.example.screenbindger.model.domain.Item
import com.example.screenbindger.model.domain.cast.CastEntity

sealed class CastsViewState {

    object Fetching : CastsViewState()
    object NotFetched : CastsViewState()
    data class Fetched(val list: List<CastEntity>) : CastsViewState()

    fun getData(): List<Item> = when (this) {
        is Fetched -> list
        else -> listOf()
    }
}