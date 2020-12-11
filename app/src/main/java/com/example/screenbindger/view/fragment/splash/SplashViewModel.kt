package com.example.screenbindger.view.fragment.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.local.db.ScreenBindgerDatabase


class SplashViewModel
@ViewModelInject
constructor(val db: ScreenBindgerDatabase) : ViewModel() {

}