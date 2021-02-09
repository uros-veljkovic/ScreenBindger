package com.example.screenbindger.view.activity.onboarding

import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.model.state.AuthState
import com.example.screenbindger.view.fragment.login.AuthorizationStateObservable
import javax.inject.Inject

abstract class AuthViewModel : ViewModel() {

    var token: String = ""

    abstract fun onboarding()
    abstract fun requestToken()
    abstract fun createSession()
    abstract fun getAccountDetails()

    abstract fun getState()
    abstract fun setState(state: AuthState)
    abstract fun setSession(session: Session)
    abstract fun getErrorMessage(): String
}