package com.example.screenbindger.view.fragment.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.screenbindger.db.local.db.ScreenBindgerDatabase


class SplashViewModel
@ViewModelInject
constructor(
    val db: ScreenBindgerDatabase
) : ViewModel() {

    fun isLoggedIn(): LiveData<Boolean?> {
        return db.isLoggedIn().asLiveData()
    }

}