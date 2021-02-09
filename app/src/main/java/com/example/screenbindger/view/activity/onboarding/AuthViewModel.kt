package com.example.screenbindger.view.activity.onboarding

import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.state.AuthState

abstract class AuthViewModel : ViewModel() {

    var token: String = ""

    abstract fun onboarding()
    abstract fun fetchToken()
    abstract fun startSession()
    abstract fun fetchAccountDetails()

    abstract fun getState()
    abstract fun setState(state: AuthState)
    abstract fun setSession(session: Session)
    abstract fun getErrorMessage(): String
}