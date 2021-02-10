package com.example.screenbindger.view.fragment.upcoming

import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.view.ListViewState

class UpcomingViewState(
    s: ListState = ListState.Init,
    l: List<MovieEntity>? = emptyList()
) : ListViewState<MovieEntity>(s, l)