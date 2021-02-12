package com.example.screenbindger.view.fragment.trending

import com.example.screenbindger.model.domain.movie.MovieEntity
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.view.ListViewState

class TrendingViewState(
    s: ListState = ListState.Init,
    l: List<MovieEntity>? = emptyList()
) : ListViewState<MovieEntity>(s, l)
