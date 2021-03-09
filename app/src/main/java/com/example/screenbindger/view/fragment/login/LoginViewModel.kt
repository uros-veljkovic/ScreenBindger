package com.example.screenbindger.view.fragment.login

import androidx.lifecycle.viewModelScope
import com.example.screenbindger.model.domain.user.UserEntity
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
    val authStateObservable: AuthorizationEventObservable
) : AuthViewModel() {

    override fun onboarding() {
        viewModelScope.launch(IO) {
            authStateObservable.setState(AuthState.Loading)
            remoteDataSource.login(user, authStateObservable)
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
