package com.example.screenbindger.db.remote.service.auth.tmdb

import com.example.screenbindger.db.remote.request.TokenRequestBody
import com.example.screenbindger.db.remote.response.AccountDetailsResponse
import com.example.screenbindger.db.remote.response.RequestTokenResponse
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.state.AuthState
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
import com.example.screenbindger.view.fragment.login.AuthorizationStateObservable
import kotlin.Exception

class TmdbAuthService(
    private val api: TmdbAuthApi
) {

    suspend fun getRequestToken(
        authStateObservable: AuthorizationStateObservable
    ) {
        api.getRequestToken().let { response ->
            if (response.isSuccessful) {
                val tokenResponse: RequestTokenResponse = response.body()!!
                authStateObservable.setToken(tokenResponse.requestToken)
                authStateObservable.setState(AuthState.TokenFetched(tokenResponse))
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
        val token = authStateObservable.getToken()

        if (token.isNullOrEmpty().not()) {
            api.postCreateSession(body = TokenRequestBody(token!!)).let { response ->
                response.apply {
                    if (isSuccessful) {
                        body()?.let {
                            val sessionId = it.sessionId
                            authStateObservable.setSessionId(sessionId)
                        }
                        authStateObservable.setState(AuthState.SessionStarted)
                    } else {
                        val errorResponse = getErrorResponse()
                        val message = errorResponse.statusMessage
                        authStateObservable.setState(
                            AuthState.Error.SessionStartFailed(
                                Exception(message)
                            )
                        )
                    }
                }
            }
        } else {
            authStateObservable.setState(AuthState.Error.TokenNotGathered(Exception("No token available for creating session")))
        }

    }

    suspend fun getAccountDetails(
        session: Session,
        authStateObservable: AuthorizationStateObservable
    ) {
        val sessionId = session.id

        api.getAccountDetails(sessionId = sessionId!!).let { response ->
            if (response.isSuccessful) {
                val accountDetails = response.body() as AccountDetailsResponse
                session.accountId = accountDetails.accountId

                authStateObservable.setState(AuthState.AccountDetailsFetched(session))
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