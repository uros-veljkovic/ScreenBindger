package com.example.screenbindger.db.remote.service.genre

import com.example.screenbindger.model.domain.GenreEntity
import retrofit2.Response
import retrofit2.http.GET

interface GenreApi {

    @GET("genre/movie/list")
    suspend fun getAll():Response<List<GenreEntity>>
}