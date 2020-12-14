package com.example.screenbindger.db.remote.service.movie

import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.util.constants.API_IMAGE_BASE_URL
import com.example.screenbindger.util.constants.POSTER_SIZE_SMALL
import retrofit2.Response
import javax.inject.Inject

class MovieService
@Inject
constructor(
    private val movieApi: MovieApi
) {

    suspend fun getTrending(): Response<List<MovieEntity>>{
        return movieApi.getTrending()
    }

    // TODO: 12/14/20 Izmeniti povratnu vrednost metode
    suspend fun getSmallPoster(movieEntity: MovieEntity): Any?{
        return movieApi.getSmallPoster("$API_IMAGE_BASE_URL/t/p/$POSTER_SIZE_SMALL/${movieEntity.smallPosterUrl}")
    }

}