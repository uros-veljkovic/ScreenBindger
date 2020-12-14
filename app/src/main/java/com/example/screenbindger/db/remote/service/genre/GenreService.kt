package com.example.screenbindger.db.remote.service.genre

import com.example.screenbindger.db.remote.response.GenresResponse
import com.example.screenbindger.model.domain.GenreEntity
import retrofit2.Response

class GenreService
constructor(
    private val genreApi: GenreApi
){

    suspend fun getAll(): Response<GenresResponse>{
        return genreApi.getAll()
    }


}