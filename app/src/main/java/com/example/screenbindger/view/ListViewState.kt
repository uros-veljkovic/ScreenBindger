package com.example.screenbindger.view

import com.example.screenbindger.model.state.ListState

abstract class ListViewState<T> (
    val state: ListState = ListState.Init,
    val list: List<T>? = null
)