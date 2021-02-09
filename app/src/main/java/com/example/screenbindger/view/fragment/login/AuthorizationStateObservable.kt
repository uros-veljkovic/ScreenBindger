package com.example.screenbindger.view.fragment.login

import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.model.state.AuthState
import com.example.screenbindger.model.state.SessionState
import com.example.screenbindger.util.state.StateObservable
import java.lang.Exception
import javax.inject.Inject

class AuthorizationStateObservable
@Inject constructor(
    val session: Session
) : StateObservable<AuthState>() {

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