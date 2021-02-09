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
    object UserCreated : AuthState()

    data class TokenFetched(val requestTokenResponse: RequestTokenResponse) : AuthState() {

        fun getToken(): String? =
            requestTokenResponse.requestToken

    }

    data class TokenAuthorized(val token: String) : AuthState()

    object ConfirmedEmail : AuthState()

    object SessionStarted : AuthState()
    data class AccountDetailsFetched(val session: Session) : AuthState()

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