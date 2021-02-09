package com.example.screenbindger.db.remote.service.movie

import com.example.screenbindger.db.remote.request.FavoriteMovieRequestBody
import com.example.screenbindger.db.remote.response.*
import com.example.screenbindger.model.domain.MovieEntity
import retrofit2.Response
import javax.inject.Inject

class MovieService
@Inject
constructor(
    private val movieApi: MovieApi
) {

    suspend fun getTrending(): Response<TrendingMoviesResponse> {
        return movieApi.getTrending()
    }

    suspend fun getUpcoming(): Response<UpcomingMoviesResponse> {
        return movieApi.getUpcoming()
    }

    suspend fun getMovieDetails(movieId: Int): Response<MovieEntity> {
        return movieApi.getMovieDetails(movieId)
    }

    suspend fun getMovieCasts(movieId: Int): Response<MovieDetailsCastResponse> {
        return movieApi.getMovieCasts(movieId)
    }

    suspend fun postMovieAsFavorite(
        sessionId: String,
        favoriteMovieRequestBody: FavoriteMovieRequestBody
    ): Response<FavoriteMovieResponse> {
        return movieApi.postMovieAsFavorite(
            sessionId = sessionId,
            favoriteMovieRequestBody = favoriteMovieRequestBody
        )
    }

}