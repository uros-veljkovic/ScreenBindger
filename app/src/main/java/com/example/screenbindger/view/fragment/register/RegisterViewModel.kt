package com.example.screenbindger.view.fragment.register

import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.model.state.RegisterState
import com.example.screenbindger.view.fragment.login.AuthorizationStateObservable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val registerStateObservable: RegisterStateObservable,
    val userStateObservable: UserStateObservable,
    val authStateObservable: AuthorizationStateObservable
) : ViewModel() {

    fun register() {
        CoroutineScope(IO).launch {
            registerStateObservable.setValue(RegisterState.Loading)
            remoteDataSource.register(userStateObservable.user, authStateObservable)
        }
    }

    fun createUser() {
        CoroutineScope(IO).launch {
            remoteDataSource.create(userStateObservable)
        }
    }

}