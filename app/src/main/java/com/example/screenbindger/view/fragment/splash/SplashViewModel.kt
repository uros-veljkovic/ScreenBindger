package com.example.screenbindger.view.fragment.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.screenbindger.db.local.repo.ScreenBindgerLocalDatabase
import javax.inject.Inject


class SplashViewModel
@Inject
constructor(
    val db: ScreenBindgerLocalDatabase
) : ViewModel() {

    fun isLoggedIn(): LiveData<Boolean?> {
        return db.isLoggedIn().asLiveData()
    }

}