package com.example.screenbindger.db.remote.repo

import com.example.screenbindger.db.remote.response.GenresResponse
import com.example.screenbindger.db.remote.response.MovieDetailsCastResponse
import com.example.screenbindger.db.remote.response.MovieDetailsResponse
import com.example.screenbindger.db.remote.response.TrendingMoviesResponse
import com.example.screenbindger.db.remote.service.genre.GenreService
import com.example.screenbindger.db.remote.service.movie.MovieService
import retrofit2.Response
import javax.inject.Inject

class ScreenBindgerRemoteDatabase
@Inject constructor(
    val movieService: MovieService,
    val genreService: GenreService
){

    suspend fun getTrending(): Response<TrendingMoviesResponse>{
        return movieService.getTrending()
    }

    suspend fun getGenres(): Response<GenresResponse>{
        return genreService.getAll()
    }

    suspend fun getMovieDetails(movieId: Int): Response<MovieDetailsResponse>{
        return movieService.getMovieDetails(movieId)
    }

    suspend fun getMovieCasts(movieId: Int): Response<MovieDetailsCastResponse>{
        return movieService.getMovieCasts(movieId)
    }

}