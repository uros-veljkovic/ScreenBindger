package com.example.screenbindger.db.remote.service.genre

import com.example.screenbindger.db.remote.response.GenreMoviesResponse
import com.example.screenbindger.db.remote.response.GenresResponse
import com.example.screenbindger.model.domain.GenreEntity
import com.example.screenbindger.util.constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

class GenreService
constructor(
    private val genreApi: GenreApi
){

    suspend fun getAll(): Response<GenresResponse>{
        return genreApi.getAll()
    }

    suspend fun getMoviesByGenre(id: Int): Response<GenreMoviesResponse>{
        return  genreApi.getMoviesByGenre(id)
    }

}