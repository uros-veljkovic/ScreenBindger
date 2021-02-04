package com.example.screenbindger.db.remote.service.auth.tmdb

import com.example.screenbindger.db.remote.response.RequestTokenResponse
import com.example.screenbindger.db.remote.response.SessionResponse
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.state.AuthState
import com.example.screenbindger.view.fragment.login.AuthorizationStateObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class TmdbAuthService(
    private val api: TmdbAuthApi
) {

    suspend fun getRequestToken(authStateObservable: AuthorizationStateObservable) {
        api.getRequestToken().let { response ->
            if (response.isSuccessful) {
                val tokenResponse = response.body()!!
                authStateObservable.setValue(AuthState.TokenGathered(tokenResponse))
            } else {
                authStateObservable.setValue(
                    AuthState.Error.TokenNotGathered(
                        Exception("Error gathering request token.")
                    )
                )
            }
        }
    }

    suspend fun createSession(
        authStateObservable: AuthorizationStateObservable
    ) {

        val tokenResponse =
            (authStateObservable.value.value as AuthState.TokenGathered).tokenResponse

        val token = tokenResponse.requestToken

        token?.let {
            api.createSession(requestToken = it).let { response ->
                if (response.isSuccessful) {
                    val session = Session().apply {
                        success = response.body()!!.success
                        id = response.body()!!.sessionId
                        expiresAt = tokenResponse.expiresAt
                    }
                    authStateObservable.setValue(AuthState.SessionStarted(session))
                } else {
                    setError(authStateObservable)
                }
            }
        } ?: setError(authStateObservable)

    }

    private fun setError(authStateObservable: AuthorizationStateObservable) {
        authStateObservable.setValue(
            AuthState.Error.SessionStartFailed(
                Exception("Error creating session. Please login/register before proceeding")
            )
        )
    }

}