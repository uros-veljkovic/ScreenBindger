package com.example.screenbindger.db.remote.service.auth.tmdb

import com.example.screenbindger.db.remote.request.TokenRequestBody
import com.example.screenbindger.db.remote.response.AccountDetailsResponse
import com.example.screenbindger.db.remote.response.RequestTokenResponse
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.state.AuthState
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
import com.example.screenbindger.view.fragment.login.AuthorizationStateObservable
import java.lang.Exception

class TmdbAuthService(
    private val api: TmdbAuthApi
) {

    suspend fun getRequestToken(
        authStateObservable: AuthorizationStateObservable
    ) {
        api.getRequestToken().let { response ->
            if (response.isSuccessful) {
                val tokenResponse: RequestTokenResponse = response.body()!!
                authStateObservable.setState(AuthState.TokenGathered(Event(tokenResponse)))
            } else {
                val errorResponse = response.getErrorResponse()
                authStateObservable.setState(
                    AuthState.Error.TokenNotGathered(
                        Exception(errorResponse.statusMessage)
                    )
                )
            }
        }
    }

    suspend fun createSession(
        authStateObservable: AuthorizationStateObservable
    ) {

        val token =
            (authStateObservable.getState() as AuthState.TokenAuthorized).token

        val body = TokenRequestBody(token)
        api.postCreateSession(body = body).let { response ->
            if (response.isSuccessful) {
                val session = Session().apply {
                    success = response.body()!!.success
                    id = response.body()!!.sessionId
                }
                authStateObservable.setState(AuthState.SessionStarted(session))
            } else {
                val errorResponse = response.getErrorResponse()
                authStateObservable.setState(
                    AuthState.Error.SessionStartFailed(
                        Exception(errorResponse.statusMessage)
                    )
                )
            }
        }
    }

    suspend fun getAccountDetails(
        authStateObservable: AuthorizationStateObservable
    ) {
        val state = authStateObservable.getState() as AuthState.SessionStarted
        val session = state.session
        val sessionId = session.id

        api.getAccountDetails(sessionId = sessionId!!).let { response ->
            if (response.isSuccessful) {
                val res = response.body() as AccountDetailsResponse
                state.session.apply {
                    accountId = res.accountId
                }
                authStateObservable.setState(AuthState.AccountDetailsGathered(Event(session)))
            } else {
                val errorResponse = response.getErrorResponse()
                authStateObservable.setState(
                    AuthState.Error.NoAccountDetails(
                        Exception(errorResponse.statusMessage)
                    )
                )
            }
        }
    }

}