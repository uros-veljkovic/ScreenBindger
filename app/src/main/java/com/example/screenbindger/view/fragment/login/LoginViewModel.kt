package com.example.screenbindger.view.fragment.login

import androidx.lifecycle.ViewModel
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.model.state.AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject


class LoginViewModel
@Inject constructor(
    val user: UserEntity,
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val authStateObservable: AuthorizationStateObservable
) : ViewModel() {

    val requestToken: String? by lazy {
        (authStateObservable.value.value as AuthState.TokenGathered).tokenResponse.requestToken
    }

    fun login() {
        CoroutineScope(IO).launch {
            authStateObservable.setValue(AuthState.Loading)
            remoteDataSource.login(user, authStateObservable)
        }
    }

    fun getToken() {
        CoroutineScope(IO).launch {
            remoteDataSource.getRequestToken(authStateObservable)
        }
    }

    fun createSession() {
        TODO("Not yet implemented")
    }

}