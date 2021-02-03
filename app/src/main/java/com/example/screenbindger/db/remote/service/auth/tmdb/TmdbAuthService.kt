package com.example.screenbindger.db.remote.service.auth.tmdb

import com.example.screenbindger.db.remote.response.RequestTokenResponse
import com.example.screenbindger.db.remote.response.SessionResponse
import retrofit2.Response

class TmdbAuthService(
    private val api: TmdbAuthApi
) {

    suspend fun getRequestToken(): Response<RequestTokenResponse> {
        return api.getRequestToken()
    }

    suspend fun createSession(requestToken: String): Response<SessionResponse> {
        return api.createSession(requestToken = requestToken)
    }

}