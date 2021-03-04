package com.example.screenbindger.db.remote.service.movie

import com.example.screenbindger.db.remote.request.MarkAsFavoriteRequestBody
import com.example.screenbindger.db.remote.response.movie.MarkAsFavoriteResponse
import com.example.screenbindger.db.remote.response.movie.CastsResponse
import com.example.screenbindger.db.remote.response.movie.MoviesResponse
import com.example.screenbindger.db.remote.response.movie.trailer.TrailersResponse
import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.util.constants.API_KEY
import retrofit2.Response
import retrofit2.http.*

interface MovieApi {

    @GET("trending/movie/week?api_key=$API_KEY")
    suspend fun getTrendingMovies(): Response<MoviesResponse>

    @GET("movie/upcoming?api_key=$API_KEY")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int
    ): Response<MoviesResponse>

    @GET("movie/{movie_id}?api_key=$API_KEY")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): Response<ShowEntity>

    @GET("movie/{movie_id}/credits?api_key=$API_KEY")
    suspend fun getMovieCasts(@Path("movie_id") movieId: Int): Response<CastsResponse>

    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavoriteMovieList(
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

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieTrailers(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<TrailersResponse>

}