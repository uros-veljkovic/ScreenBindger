package com.example.screenbindger.db.remote.service.genre

import com.example.screenbindger.db.remote.response.GenreMoviesResponse
import com.example.screenbindger.db.remote.response.GenresResponse
import com.example.screenbindger.util.constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GenreApi {

    @GET("genre/movie/list?api_key=$API_KEY")
    suspend fun getAll():Response<GenresResponse>

    @GET("discover/movie?api_key=${API_KEY}")
    suspend fun getMoviesByGenre(@Query("with_genres")id: String): Response<GenreMoviesResponse>
}