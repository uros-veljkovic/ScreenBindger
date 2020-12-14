package com.example.screenbindger.view.activity.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDatabase
import com.example.screenbindger.model.global.Genres
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainViewModel
@ViewModelInject constructor(
    val db: ScreenBindgerRemoteDatabase
) : ViewModel() {

    fun fetchGenres() {
        CoroutineScope(IO).launch {
            Genres.list.addAll(db.getGenres().body()?.list ?: mutableListOf())
        }
    }

}