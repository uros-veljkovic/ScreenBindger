package com.example.screenbindger.model.state

import com.example.screenbindger.db.remote.response.RequestTokenResponse
import com.example.screenbindger.db.remote.session.Session
import java.lang.Exception

sealed class AuthState<out R> {

    data class FirebaseAuthSuccess<out T>(val data: T? = null) : AuthState<T>()
    data class TokenGathered(val tokenResponse: RequestTokenResponse) : AuthState<Nothing>()
    object TokenAuthorized : AuthState<Nothing>()
    data class SessionStarted(val session: Session) : AuthState<Nothing>()

    sealed class Error(val exception: Exception) : AuthState<Nothing>() {
        data class FirebaseAuthFailed(val e: Exception) : Error(e)
        data class TokenNotGathered(val e: Exception) : Error(e)
        data class TokenNotAuthorized(val e: Exception) : Error(e)
        data class SessionStartFailed(val e: Exception) : Error(e)
    }

    object Loading : AuthState<Nothing>()
    object Rest : AuthState<Nothing>()
}