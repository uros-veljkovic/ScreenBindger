package com.example.screenbindger.db.remote.service.tv_show

import com.example.screenbindger.db.remote.response.movie.CastsResponse
import com.example.screenbindger.db.remote.response.movie.MoviesResponse
import com.example.screenbindger.db.remote.response.movie.trailer.TrailersResponse
import com.example.screenbindger.db.remote.response.tv_show.UpcomingTvShowResponse
import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.util.constants.API_KEY
import retrofit2.Response
import retrofit2.http.*

interface TvShowApi {

    @GET("tv/on_the_air")
    suspend fun getUpcoming(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): Response<UpcomingTvShowResponse>

    @GET("tv/popular")
    suspend fun getTrending(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<UpcomingTvShowResponse>

    @GET("tv/{tv_id}")
    suspend fun getDetails(
        @Path("tv_id") id: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<ShowEntity>

    @GET("tv/{tv_id}/credits")
    suspend fun getCasts(
        @Path("tv_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<CastsResponse>

    @GET("tv/{tv_id}/videos")
    suspend fun getTrailers(
        @Path("tv_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<TrailersResponse>

    @GET("account/{account_id}/favorite/tv")
    suspend fun getFavoriteTvShowList(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MoviesResponse>


}