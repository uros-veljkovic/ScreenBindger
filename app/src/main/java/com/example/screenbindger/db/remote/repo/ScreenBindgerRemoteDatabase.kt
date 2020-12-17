package com.example.screenbindger.db.remote.repo

import com.example.screenbindger.db.remote.response.*
import com.example.screenbindger.db.remote.service.genre.GenreService
import com.example.screenbindger.db.remote.service.movie.MovieService
import com.example.screenbindger.model.domain.MovieEntity
import retrofit2.Response
import javax.inject.Inject

class ScreenBindgerRemoteDatabase
@Inject constructor(
    val movieService: MovieService,
    val genreService: GenreService
) {

    suspend fun getTrending(): Response<TrendingMoviesResponse> {
        return movieService.getTrending()
    }

    suspend fun getUpcoming(): Response<UpcomingMoviesResponse> {
        return movieService.getUpcoming()
    }

    suspend fun getGenres(): Response<GenresResponse> {
        return genreService.getAll()
    }

    suspend fun getMovieDetails(movieId: Int): Response<MovieEntity> {
        return movieService.getMovieDetails(movieId)
    }

    suspend fun getMovieCasts(movieId: Int): Response<MovieDetailsCastResponse> {
        return movieService.getMovieCasts(movieId)
    }

    suspend fun getMoviesByGenre(id: Int): Response<GenreMoviesResponse> {
        return genreService.getMoviesByGenre(id)
    }


}