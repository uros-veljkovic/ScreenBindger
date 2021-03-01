package com.example.screenbindger.db.remote.service.genre

import com.example.screenbindger.db.remote.response.movie.MoviesByGenreResponse
import com.example.screenbindger.db.remote.response.genre.AllGenresResponse
import com.example.screenbindger.db.remote.response.movie.MoviesResponse
import com.example.screenbindger.util.constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GenreApi {

    @GET("genre/movie/list?api_key=$API_KEY")
    suspend fun getAllGenres():Response<AllGenresResponse>

    @GET("discover/movie?api_key=${API_KEY}")
    suspend fun getMoviesByGenre(@Query("with_genres")id: String): Response<MoviesResponse>
}