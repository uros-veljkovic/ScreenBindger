package com.example.screenbindger.db.remote.service.movie

import com.example.screenbindger.db.remote.request.MarkAsFavoriteRequestBody
import com.example.screenbindger.db.remote.response.movie.FavoriteMovieListResponse
import com.example.screenbindger.db.remote.response.movie.MarkAsFavoriteResponse
import com.example.screenbindger.db.remote.response.movie.MovieCastsResponse
import com.example.screenbindger.db.remote.response.movie.TrendingMoviesResponse
import com.example.screenbindger.db.remote.response.movie.UpcomingMoviesResponse
import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.util.constants.API_KEY
import retrofit2.Response
import retrofit2.http.*

interface MovieApi {

    @GET("trending/movie/week?api_key=$API_KEY")
    suspend fun getTrendingMovies(): Response<TrendingMoviesResponse>

    @GET("movie/upcoming?api_key=$API_KEY")
    suspend fun getUpcomingMovies(): Response<UpcomingMoviesResponse>

    @GET
    suspend fun getSmallPoster(@Url url: String)

    @GET("movie/{movie_id}?api_key=$API_KEY")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): Response<MovieEntity>

    @GET("movie/{movie_id}/credits?api_key=$API_KEY")
    suspend fun getMovieCasts(@Path("movie_id") movieId: Int): Response<MovieCastsResponse>

    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavoriteMovieList(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<FavoriteMovieListResponse>

    @Headers("Content-type: application/json;charset=utf-8")
    @POST("account/{account_id}/favorite")
    suspend fun postMarkAsFavorite(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String = API_KEY,
        @Body body: MarkAsFavoriteRequestBody
    ): Response<MarkAsFavoriteResponse>

}