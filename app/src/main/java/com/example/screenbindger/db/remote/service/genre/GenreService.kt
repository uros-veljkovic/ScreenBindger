package com.example.screenbindger.db.remote.service.genre

import com.example.screenbindger.db.remote.response.MoviesByGenreResponse
import com.example.screenbindger.db.remote.response.AllGenresResponse
import retrofit2.Response

class GenreService
constructor(
    private val genreApi: GenreApi
){

    suspend fun getAll(): Response<AllGenresResponse>{
        return genreApi.getAllGenres()
    }

    suspend fun getMoviesByGenre(id: String): Response<MoviesByGenreResponse>{
        return  genreApi.getMoviesByGenre(id)
    }

}