package com.example.screenbindger.view.fragment.upcoming

import com.example.screenbindger.model.domain.movie.MovieEntity
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.view.ListViewState

class UpcomingFragmentViewState(
    state: ListState = ListState.Init,
    list: List<MovieEntity>? = emptyList()
) : ListViewState<MovieEntity>(state, list)