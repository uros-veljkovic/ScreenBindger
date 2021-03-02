package com.example.screenbindger.db.remote.service.favorites

import com.example.screenbindger.db.remote.request.MarkAsFavoriteRequestBody
import com.example.screenbindger.db.remote.response.movie.MarkAsFavoriteResponse
import com.example.screenbindger.db.remote.response.movie.MoviesResponse
import com.example.screenbindger.util.constants.API_KEY
import retrofit2.Response
import retrofit2.http.*

interface FavoritesApi {

    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavoriteMovieList(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MoviesResponse>

    @GET("account/{account_id}/favorite/tv")
    suspend fun getFavoriteTvShowList(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MoviesResponse>

    @Headers("Content-type: application/json;charset=utf-8")
    @POST("account/{account_id}/favorite")
    suspend fun postMarkAsFavorite(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String = API_KEY,
        @Body body: MarkAsFavoriteRequestBody
    ): Response<MarkAsFavoriteResponse>

}