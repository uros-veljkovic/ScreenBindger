package com.example.screenbindger.view.fragment.register

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.local.repo.ScreenBindgerLocalDatabase
import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel
@ViewModelInject constructor(
    val db: ScreenBindgerLocalDatabase,
    val user: UserObservable
) : ViewModel(){

    fun register(){
        CoroutineScope(Dispatchers.IO).launch {
            db.register(user.toEntity())
        }
    }

}