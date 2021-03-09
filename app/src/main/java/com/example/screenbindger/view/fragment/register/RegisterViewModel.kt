package com.example.screenbindger.view.fragment.register

import androidx.lifecycle.viewModelScope
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
        viewModelScope.launch(IO) {
            remoteDataSource.register(userStateObservable.user, authStateObservable)
        }
    }

    fun createUser() {
        viewModelScope.launch(IO) {
            remoteDataSource.create(userStateObservable)
        }
    }

    override fun fetchToken() {
        viewModelScope.launch(IO) {
            remoteDataSource.getRequestToken(authStateObservable)
        }
    }

    override fun startSession() {
        viewModelScope.launch(IO) {
            remoteDataSource.createSession(authStateObservable)
        }
    }

    override fun fetchAccountDetails() {
        viewModelScope.launch(IO) {
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
