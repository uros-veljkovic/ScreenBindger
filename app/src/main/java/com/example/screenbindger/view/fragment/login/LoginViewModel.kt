package com.example.screenbindger.view.fragment.login

import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDatabase
import com.example.screenbindger.db.remote.service.auth.AuthStateObservable
import com.example.screenbindger.util.state.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class LoginViewModel
@Inject constructor(
    val user: UserObservable,
    val remoteDb: ScreenBindgerRemoteDatabase,
    val stateObservable: AuthStateObservable
) : ViewModel() {

    fun login() {
        CoroutineScope(Dispatchers.IO).launch {
            stateObservable.setValue(State.Loading)
            remoteDb.login(user, stateObservable)
        }
    }

}