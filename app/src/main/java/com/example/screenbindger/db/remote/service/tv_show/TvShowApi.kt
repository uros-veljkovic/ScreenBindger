package com.example.screenbindger.db.remote.service.tv_show

import com.example.screenbindger.db.remote.response.tv_show.UpcomingTvShowResponse
import com.example.screenbindger.util.constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TvShowApi {

    @GET("tv/on_the_air")
    suspend fun getUpcoming(
        @Query("api_key") apiKey: String = API_KEY
    ): Response<UpcomingTvShowResponse>

}