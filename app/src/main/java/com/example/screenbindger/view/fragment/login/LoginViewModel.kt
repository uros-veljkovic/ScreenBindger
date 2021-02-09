package com.example.screenbindger.view.fragment.login

import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.state.AuthState
import com.example.screenbindger.view.activity.onboarding.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    val user: UserEntity,
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val authStateObservable: AuthorizationStateObservable
) : AuthViewModel() {

    override fun onboarding() {
        CoroutineScope(IO).launch {
            authStateObservable.setState(AuthState.Loading)
            remoteDataSource.login(user, authStateObservable)
        }
    }

    override fun requestToken() {
        CoroutineScope(IO).launch {
            remoteDataSource.getRequestToken(authStateObservable)
        }
    }

    override fun createSession() {
        CoroutineScope(IO).launch {
            remoteDataSource.createSession(authStateObservable)
        }
    }

    override fun getAccountDetails() {
        CoroutineScope(IO).launch {
            remoteDataSource.getAccountDetails(authStateObservable)
        }
    }

    override fun getState() {
        authStateObservable.getState()
    }

    override fun setState(state: AuthState) {
        authStateObservable.setState(state)
    }

    override fun setSession(session: Session) {
        remoteDataSource.setSession(session)
    }

    override fun getErrorMessage(): String {
        val e = authStateObservable.getState() as AuthState.Error
        return e.getMessage() ?: "Unknown error"
    }

}
/*
class LoginViewModel
@Inject constructor(
    val user: UserEntity,
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val authStateObservable: AuthorizationStateObservable
) : ViewModel() {

    var token: String = ""

    fun login() {
        CoroutineScope(IO).launch {
            authStateObservable.setValue(AuthState.Loading)
            remoteDataSource.login(user, authStateObservable)
        }
    }

    fun requestToken() {
        CoroutineScope(IO).launch {
            remoteDataSource.getRequestToken(authStateObservable)
        }
    }

    fun createSession() {
        CoroutineScope(IO).launch {
            remoteDataSource.createSession(authStateObservable)
        }
    }

    fun getAccountDetails() {
        CoroutineScope(IO).launch {
            remoteDataSource.getAccountDetails(authStateObservable)
        }
    }

    fun getState(): AuthState = authStateObservable.getState()

    fun setState(state: AuthState) {
        authStateObservable.setValue(state)
    }

    fun setSession(session: Session) {
        remoteDataSource.setSession(session)
    }

    fun getErrorMessage(): String {
        val e = authStateObservable.getState() as AuthState.Error
        return e.getMessage() ?: "Unknown error"
    }


}*/
