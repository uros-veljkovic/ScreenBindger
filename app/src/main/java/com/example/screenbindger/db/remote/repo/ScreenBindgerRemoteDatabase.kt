package com.example.screenbindger.db.remote.repo

import android.net.Uri
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.db.remote.response.*
import com.example.screenbindger.db.remote.service.auth.AuthStateObservable
import com.example.screenbindger.db.remote.service.auth.AuthService
import com.example.screenbindger.db.remote.service.genre.GenreService
import com.example.screenbindger.db.remote.service.movie.MovieService
import com.example.screenbindger.db.remote.service.storage.StorageService
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.db.remote.service.user.UserService
import com.example.screenbindger.model.domain.MovieEntity
import retrofit2.Response
import javax.inject.Inject

class ScreenBindgerRemoteDatabase
@Inject constructor(
    private val movieService: MovieService,
    private val genreService: GenreService,
    private val authService: AuthService,
    private val userService: UserService,
    private val storageService: StorageService

) {

    suspend fun login(
        user: UserEntity,
        stateObservable: AuthStateObservable
    ) {
        authService.signIn(user.email, user.password, stateObservable)
    }

    suspend fun register(
        user: UserEntity,
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

    suspend fun create(userStateObservable: UserStateObservable) {
        userService.create(userStateObservable)
    }

    suspend fun changePassword(
        newPassword: String,
        userStateObservable: UserStateObservable
    ) {
        authService.changePassword(newPassword, userStateObservable)
    }

    suspend fun fetchUser(userStateObservable: UserStateObservable) {
        userService.read(userStateObservable)
    }

    suspend fun updateUser(userStateObservable: UserStateObservable) {
        userService.update(userStateObservable)
    }

    suspend fun uploadImage(uri: Uri, userStateObservable: UserStateObservable) {
        storageService.uploadImage(uri, userStateObservable)
    }

    suspend fun fetchProfilePicture(userStateObservable: UserStateObservable) {
        storageService.downloadImage(userStateObservable)
    }


}