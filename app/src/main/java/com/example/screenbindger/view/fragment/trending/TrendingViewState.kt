package com.example.screenbindger.view.fragment.trending

import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.model.state.ListState

data class TrendingViewState(
    val state: ListState,
    val list: List<MovieEntity>? = null
){

}
