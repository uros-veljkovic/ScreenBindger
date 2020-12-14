package com.example.screenbindger.db.remote.service.genre

import com.example.screenbindger.db.remote.response.GenresResponse
import com.example.screenbindger.util.constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET

interface GenreApi {

    @GET("genre/movie/list?api_key=$API_KEY")
    suspend fun getAll():Response<GenresResponse>
}