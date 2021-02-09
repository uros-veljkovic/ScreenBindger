package com.example.screenbindger.view.fragment.login

import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.state.AuthState
import com.example.screenbindger.util.event.EventObservable
import javax.inject.Inject

class AuthorizationEventObservable
@Inject constructor(
    val session: Session
) : EventObservable<AuthState>() {

    fun getState(): AuthState =
        this.value.value?.getContentIfNotHandled()
            ?: AuthState.Rest

    fun getToken(): String? = session.requestToken

    fun setToken(requestToken: String?) {
        session.requestToken = requestToken
    }

    fun setSessionId(sessionId: String) {
        session.id = sessionId
    }

}