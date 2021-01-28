package com.example.screenbindger.view.fragment.register

import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.local.repo.ScreenBindgerLocalDatabase
import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDatabase
import com.example.screenbindger.db.remote.service.auth.AuthStateObservable
import com.example.screenbindger.db.remote.service.user.UserActionStateObservable
import com.example.screenbindger.util.state.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel
@Inject constructor(
    val localDb: ScreenBindgerLocalDatabase,
    val remoteDb: ScreenBindgerRemoteDatabase,
    val user: UserObservable,
    val authStateObservable: AuthStateObservable,
    val userActionStateObservable: UserActionStateObservable
) : ViewModel() {

    fun register() {
        CoroutineScope(Dispatchers.IO).launch {
            authStateObservable.setValue(State.Loading)

            remoteDb.register(user, authStateObservable)
            remoteDb.create(user, userActionStateObservable)
        }
    }

}