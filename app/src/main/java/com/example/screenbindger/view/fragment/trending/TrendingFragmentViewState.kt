package com.example.screenbindger.view.fragment.trending

import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.view.ListViewState

class TrendingFragmentViewState(
    state: ListState = ListState.Fetching,
    list: List<ShowEntity>? = emptyList()
) : ListViewState<ShowEntity>(state, list)
