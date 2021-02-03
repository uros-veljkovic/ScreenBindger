package com.example.screenbindger.view.fragment.login

import androidx.lifecycle.ViewModel
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.service.auth.firebase.FirebaseAuthState
import com.example.screenbindger.model.state.LoginState
import com.example.screenbindger.util.state.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class LoginViewModel
@Inject constructor(
    val user: UserEntity,
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val loginStateObservable: LoginStateObservable
) : ViewModel() {

    fun login() {
        CoroutineScope(Dispatchers.IO).launch {
            loginStateObservable.setValue(LoginState.Loading)
            remoteDataSource.login(user, loginStateObservable)
        }
    }

}