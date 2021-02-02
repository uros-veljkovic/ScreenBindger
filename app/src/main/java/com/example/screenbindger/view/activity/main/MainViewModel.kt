package com.example.screenbindger.view.activity.main

import androidx.lifecycle.ViewModel
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDatabase
import com.example.screenbindger.model.global.Genres
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel
@Inject constructor(
    val db: ScreenBindgerRemoteDatabase,
    val user: UserEntity
) : ViewModel() {

    fun fetchGenres() {
        CoroutineScope(IO).launch {
            Genres.list.addAll(db.getGenres().body()?.list ?: mutableListOf())
        }
    }

}