package com.example.screenbindger.model.state

import com.example.screenbindger.db.remote.response.RequestTokenResponse
import com.example.screenbindger.db.remote.response.ValidateRequestTokenResponse
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.util.event.Event
import java.lang.Error
import java.lang.Exception

sealed class AuthState {

    data class FirebaseAuthSuccess(val data: UserEntity? = null) : AuthState()

    data class TokenGathered(val event: Event<RequestTokenResponse>) : AuthState()
    data class TokenAuthorized(val token: String) : AuthState()

    data class SessionStarted(val session: Session) : AuthState()
    data class AccountDetailsGathered(val session: Event<Session>) : AuthState()

    sealed class Error(val exception: Exception) : AuthState() {
        data class NoState(val e: Exception) : Error(e)
        data class FirebaseAuthFailed(val e: Exception) : Error(e)
        data class TokenNotGathered(val e: Exception) : Error(e)
        data class TokenNotAuthorized(val e: Exception) : Error(e)
        data class SessionStartFailed(val e: Exception) : Error(e)
        data class NoAccountDetails(val e: Exception) : Error(e)

        fun getMessage() = exception.message
    }

    object Loading : AuthState()
    object Rest : AuthState()
}