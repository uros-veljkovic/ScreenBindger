package com.example.screenbindger.view.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.screenbindger.model.domain.user.UserEntity
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.model.global.Genres
import com.example.screenbindger.view.fragment.genres.GenresViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel
@Inject constructor(
    val db: ScreenBindgerRemoteDataSource,
    val user: UserEntity
) : ViewModel() {

    fun fetchGenres() {
        viewModelScope.launch {
            when (val state = db.getGenres()) {
                is GenresViewState.Fetched -> {
                    Genres.list.clear()
                    Genres.list.addAll(state.list)
                }
                is GenresViewState.NotFetched -> {
                    Genres.list.addAll(emptyList())
                }
                else -> {
                }
            }
        }
    }

}