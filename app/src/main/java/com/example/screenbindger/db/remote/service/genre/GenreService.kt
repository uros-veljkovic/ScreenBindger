package com.example.screenbindger.db.remote.service.genre

import com.example.screenbindger.db.remote.response.GenreMoviesResponse
import com.example.screenbindger.db.remote.response.GenresResponse
import retrofit2.Response

class GenreService
constructor(
    private val genreApi: GenreApi
){

    suspend fun getAll(): Response<GenresResponse>{
        return genreApi.getAll()
    }

    suspend fun getMoviesByGenre(id: String): Response<GenreMoviesResponse>{
        return  genreApi.getMoviesByGenre(id)
    }

}