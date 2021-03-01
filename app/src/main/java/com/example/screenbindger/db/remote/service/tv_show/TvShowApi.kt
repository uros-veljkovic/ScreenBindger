package com.example.screenbindger.db.remote.service.tv_show

import com.example.screenbindger.db.remote.response.movie.CastsResponse
import com.example.screenbindger.db.remote.response.movie.trailer.TrailersResponse
import com.example.screenbindger.db.remote.response.tv_show.UpcomingTvShowResponse
import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.util.constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowApi {

    @GET("tv/on_the_air")
    suspend fun getUpcoming(
        @Query("api_key") apiKey: String = API_KEY
    ): Response<UpcomingTvShowResponse>

    @GET("tv/popular")
    suspend fun getTrending(
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

    @GET("movie/{movie_id}/videos")
    suspend fun getTrailers(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<TrailersResponse>


}