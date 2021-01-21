package com.example.screenbindger.view.fragment.register

import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.local.repo.ScreenBindgerLocalDatabase
import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel
@Inject constructor(
    val db: ScreenBindgerLocalDatabase,
    val user: UserObservable
) : ViewModel(){

    fun register(){
        CoroutineScope(Dispatchers.IO).launch {
            db.register(user.toEntity())
        }
    }

}