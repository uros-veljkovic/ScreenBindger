package com.example.screenbindger.db.remote.service.movie

import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.util.constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface MovieApi {

    @GET("trending/movie/week")
    suspend fun getTrending(@Path("api_key") apiKey: String = API_KEY): Response<List<MovieEntity>>

    @GET
    suspend fun getSmallPoster(@Url url: String)

}