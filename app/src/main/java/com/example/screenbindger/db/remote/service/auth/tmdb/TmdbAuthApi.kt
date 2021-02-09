package com.example.screenbindger.db.remote.service.auth.tmdb

import com.example.screenbindger.db.remote.request.TokenRequestBody
import com.example.screenbindger.db.remote.request.ValidateRequestTokenRequestBody
import com.example.screenbindger.db.remote.response.AccountDetailsResponse
import com.example.screenbindger.db.remote.response.RequestTokenResponse
import com.example.screenbindger.db.remote.response.SessionResponse
import com.example.screenbindger.db.remote.response.ValidateRequestTokenResponse
import com.example.screenbindger.util.constants.API_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TmdbAuthApi {

    @GET("authentication/token/new")
    suspend fun getRequestToken(
        @Query("api_key") apiKey: String = API_KEY
    ): Response<RequestTokenResponse>

    @POST("authentication/session/new")
    suspend fun postCreateSession(
        @Query("api_key") apiKey: String = API_KEY,
        @Body body: TokenRequestBody
    ): Response<SessionResponse>

    @GET("account")
    suspend fun getAccountDetails(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("session_id") sessionId: String
    ): Response<AccountDetailsResponse>


/*    @POST("authentication/token/validate_with_login")
    suspend fun validateRequestToken(
        @Query("api_key") apiKey: String = API_KEY,
        @Body body: ValidateRequestTokenRequestBody
    ): Response<ValidateRequestTokenResponse>*/
}