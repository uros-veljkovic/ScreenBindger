package com.example.screenbindger.view.fragment.register

import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.state.AuthState
import com.example.screenbindger.view.activity.onboarding.AuthViewModel
import com.example.screenbindger.view.fragment.login.AuthorizationEventObservable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel
@Inject constructor(
    val userStateObservable: UserStateObservable,
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val authStateObservable: AuthorizationEventObservable
) : AuthViewModel() {

    override fun onboarding() {
        startLoading()
        registerUser()
    }

    private fun startLoading() {
        authStateObservable.setState(AuthState.Loading)
    }

    private fun registerUser() {
        CoroutineScope(IO).launch {
            remoteDataSource.register(userStateObservable.user, authStateObservable)
        }
    }

    fun createUser() {
        CoroutineScope(IO).launch {
            remoteDataSource.create(userStateObservable)
        }
    }

    override fun fetchToken() {
        CoroutineScope(IO).launch {
            remoteDataSource.getRequestToken(authStateObservable)
        }
    }

    override fun startSession() {
        CoroutineScope(IO).launch {
            remoteDataSource.createSession(authStateObservable)
        }
    }

    override fun fetchAccountDetails() {
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
class RegisterViewModel
@Inject constructor(
    val user: UserEntity,
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val authStateObservable: AuthorizationStateObservable
) : ViewModel() {

    fun register() {
        startLoading()
        createUser()
        CoroutineScope(IO).launch {
            remoteDataSource.register(user, authStateObservable)
        }
    }

    private fun startLoading() {
        authStateObservable.setState(AuthState.Loading)
    }

    fun createUser() {
        CoroutineScope(IO).launch {
//            remoteDataSource.create()
        }
    }

    fun getRequestToken() {
        CoroutineScope(IO).launch {
            remoteDataSource.getRequestToken(authStateObservable)
        }
    }

    fun createSession() {

    }

    fun setState(state: AuthState) {
        authStateObservable.setState(state)
    }

}*/
