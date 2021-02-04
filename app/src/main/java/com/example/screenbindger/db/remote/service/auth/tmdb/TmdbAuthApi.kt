package com.example.screenbindger.db.remote.service.auth.tmdb

import com.example.screenbindger.db.remote.response.RequestTokenResponse
import com.example.screenbindger.db.remote.response.SessionResponse
import com.example.screenbindger.util.constants.API_KEY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TmdbAuthApi {

    @GET("authentication/token/new")
    suspend fun getRequestToken(@Query("api_key") apiKey: String = API_KEY): Response<RequestTokenResponse>

    @POST("authentication/session/new")
    suspend fun createSession(
        @Query("api_key") apiKey: String = API_KEY,
        @Body requestToken: String
    ): Response<SessionResponse>
}