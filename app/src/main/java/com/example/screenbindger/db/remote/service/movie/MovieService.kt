package com.example.screenbindger.db.remote.service.movie

import com.example.screenbindger.db.remote.response.MovieDetailsCastResponse
import com.example.screenbindger.db.remote.response.MovieDetailsResponse
import com.example.screenbindger.db.remote.response.TrendingMoviesResponse
import com.example.screenbindger.db.remote.response.UpcomingMoviesResponse
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

    suspend fun getTrending(): Response<TrendingMoviesResponse>{
        return movieApi.getTrending()
    }

    suspend fun getUpcoming(): Response<UpcomingMoviesResponse>{
        return movieApi.getUpcoming()
    }

    suspend fun getMovieDetails(movieId: Int): Response<MovieEntity>{
        return movieApi.getMovieDetails(movieId)
    }

    suspend fun getMovieCasts(movieId: Int): Response<MovieDetailsCastResponse>{
        return movieApi.getMovieCasts(movieId)
    }

}