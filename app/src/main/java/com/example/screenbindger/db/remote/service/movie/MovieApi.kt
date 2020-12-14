package com.example.screenbindger.db.remote.service.movie

import com.example.screenbindger.db.remote.response.TrendingMoviesResponse
import com.example.screenbindger.util.constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface MovieApi {

    @GET("trending/movie/week?api_key=$API_KEY")
    suspend fun getTrending(): Response<TrendingMoviesResponse>

    @GET
    suspend fun getSmallPoster(@Url url: String)

}