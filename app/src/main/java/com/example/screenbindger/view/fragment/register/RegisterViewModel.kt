package com.example.screenbindger.view.fragment.register

import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDatabase
import com.example.screenbindger.db.remote.service.auth.AuthStateObservable
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.util.state.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel
@Inject constructor(
    val remoteDb: ScreenBindgerRemoteDatabase,
    val authStateObservable: AuthStateObservable,
    val userStateObservable: UserStateObservable
) : ViewModel() {

    fun register() {
        CoroutineScope(Dispatchers.IO).launch {
            authStateObservable.setValue(State.Loading)

            remoteDb.register(userStateObservable.user, authStateObservable)
            remoteDb.create(userStateObservable)
        }
    }

}