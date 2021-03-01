package com.example.screenbindger.view.fragment.upcoming

import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.view.ListViewState

class UpcomingFragmentViewState(
    state: ListState = ListState.Init,
    list: List<ShowEntity>? = emptyList()
) : ListViewState<ShowEntity>(state, list)