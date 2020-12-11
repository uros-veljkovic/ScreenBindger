package com.example.screenbindger.view.fragment.register

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.local.db.ScreenBindgerDatabase
import com.example.screenbindger.db.local.entity.user.UserEntity
import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel
@ViewModelInject constructor(
    val db: ScreenBindgerDatabase,
    val user: UserObservable
) : ViewModel(){

    fun register(){
        CoroutineScope(Dispatchers.IO).launch {
            db.register(user.toEntity())
        }
    }

}