package com.example.screenbindger.view.fragment.register

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.local.db.ScreenBindgerDatabase
import com.example.screenbindger.db.local.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel
@ViewModelInject constructor(
    val db: ScreenBindgerDatabase
) : ViewModel(){

    fun register(userEntity: UserEntity){
        CoroutineScope(Dispatchers.IO).launch {
            db.register(userEntity)
        }
    }

}