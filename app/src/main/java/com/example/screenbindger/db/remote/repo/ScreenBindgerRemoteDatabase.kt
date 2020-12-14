package com.example.screenbindger.db.remote.repo

import com.example.screenbindger.db.remote.response.TrendingMoviesResponse
import com.example.screenbindger.db.remote.service.genre.GenreService
import com.example.screenbindger.db.remote.service.movie.MovieService
import com.example.screenbindger.model.domain.GenreEntity
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

    suspend fun getGenres(): Response<List<GenreEntity>>{
        return genreService.getAll()
    }

}