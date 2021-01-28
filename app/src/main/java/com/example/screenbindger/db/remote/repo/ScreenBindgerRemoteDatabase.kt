package com.example.screenbindger.db.remote.repo

import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import com.example.screenbindger.db.remote.response.*
import com.example.screenbindger.db.remote.service.auth.AuthStateObservable
import com.example.screenbindger.db.remote.service.auth.AuthService
import com.example.screenbindger.db.remote.service.genre.GenreService
import com.example.screenbindger.db.remote.service.movie.MovieService
import com.example.screenbindger.db.remote.service.user.UserActionStateObservable
import com.example.screenbindger.db.remote.service.user.UserService
import com.example.screenbindger.model.domain.MovieEntity
import retrofit2.Response
import javax.inject.Inject

class ScreenBindgerRemoteDatabase
@Inject constructor(
    val movieService: MovieService,
    val genreService: GenreService,
    val authService: AuthService,
    val userService: UserService
) {

    suspend fun login(
        user: UserObservable,
        stateObservable: AuthStateObservable
    ) {
        authService.signIn(user.email, user.password, stateObservable)
    }

    suspend fun register(
        user: UserObservable,
        stateObservable: AuthStateObservable
    ) {
        authService.signUp(user.email, user.password, stateObservable)
    }


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

    suspend fun getMoviesByGenre(id: String): Response<GenreMoviesResponse> {
        return genreService.getMoviesByGenre(id)
    }

    suspend fun create(user: UserObservable, userActionStateObservable: UserActionStateObservable) {
        userService.create(user, userActionStateObservable)
    }


}