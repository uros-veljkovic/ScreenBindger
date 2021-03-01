package com.example.screenbindger.db.remote.service.genre

import com.example.screenbindger.db.remote.response.movie.MoviesByGenreResponse
import com.example.screenbindger.db.remote.response.genre.AllGenresResponse
import com.example.screenbindger.db.remote.response.movie.MoviesResponse
import retrofit2.Response

class GenreService
constructor(
    private val genreApi: GenreApi
) {

    suspend fun getAll(): Response<AllGenresResponse> {
        return genreApi.getAllGenres()
    }

    suspend fun getMoviesByGenre(id: String): Response<MoviesResponse> {
        return genreApi.getMoviesByGenre(id)
    }

}