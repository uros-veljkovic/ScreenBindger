package com.example.screenbindger.db.remote.service.movie

import com.example.screenbindger.db.remote.response.MovieDetailsCastResponse
import com.example.screenbindger.db.remote.response.MovieDetailsResponse
import com.example.screenbindger.db.remote.response.TrendingMoviesResponse
import com.example.screenbindger.db.remote.response.UpcomingMoviesResponse
import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.util.constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface MovieApi {

    @GET("trending/movie/week?api_key=$API_KEY")
    suspend fun getTrending(): Response<TrendingMoviesResponse>

    @GET("movie/upcoming?api_key=$API_KEY")
    suspend fun getUpcoming(): Response<UpcomingMoviesResponse>

    @GET
    suspend fun getSmallPoster(@Url url: String)

    @GET("movie/{movie_id}?api_key=$API_KEY")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): Response<MovieEntity>

    @GET("movie/{movie_id}/credits?api_key=$API_KEY")
    suspend fun getMovieCasts(@Path("movie_id") movieId: Int): Response<MovieDetailsCastResponse>

}