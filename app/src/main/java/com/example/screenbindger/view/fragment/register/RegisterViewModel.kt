package com.example.screenbindger.view.fragment.register

import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.service.auth.firebase.FirebaseAuthState
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.model.state.RegisterState
import com.example.screenbindger.util.state.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val registerStateObservable: RegisterStateObservable,
    val userStateObservable: UserStateObservable
) : ViewModel() {

    fun register() {
        CoroutineScope(IO).launch {
            registerStateObservable.setValue(RegisterState.Loading)
            remoteDataSource.register(userStateObservable.user, registerStateObservable)
        }
    }

    fun createUser() {
        CoroutineScope(IO).launch {
            remoteDataSource.create(userStateObservable)
        }
    }

}