package com.example.screenbindger.db.remote.repo

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.db.remote.request.MarkAsFavoriteRequestBody
import com.example.screenbindger.model.domain.user.UserEntity
import com.example.screenbindger.db.remote.response.genre.AllGenresResponse
import com.example.screenbindger.db.remote.response.movie.MoviesResponse
import com.example.screenbindger.db.remote.service.auth.firebase.AuthService
import com.example.screenbindger.db.remote.service.auth.tmdb.TmdbAuthService
import com.example.screenbindger.db.remote.service.favorites.FavoritesService
import com.example.screenbindger.db.remote.service.genre.GenreService
import com.example.screenbindger.db.remote.service.movie.MovieService
import com.example.screenbindger.db.remote.service.review.ReviewService
import com.example.screenbindger.db.remote.service.storage.StorageService
import com.example.screenbindger.db.remote.service.tv_show.TvShowService
import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.db.remote.service.user.UserService
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.view.fragment.details.*
import com.example.screenbindger.view.fragment.favorite_movies.FavoritesViewEvent
import com.example.screenbindger.view.fragment.login.AuthorizationEventObservable
import com.example.screenbindger.view.fragment.review.ReviewViewEvent
import com.example.screenbindger.view.fragment.ShowListViewState
import retrofit2.Response
import javax.inject.Inject

class ScreenBindgerRemoteDataSource
@Inject constructor(
    private val session: Session,
    private val movieService: MovieService,
    private val tvShowService: TvShowService,
    private val genreService: GenreService,
    private val tmdbAuthService: TmdbAuthService,
    private val authService: AuthService,
    private val userService: UserService,
    private val storageService: StorageService,
    private val reviewService: ReviewService,
    private val favoritesService: FavoritesService
) {

    suspend fun login(
        user: UserEntity,
        authStateObservable: AuthorizationEventObservable
    ) {
        authService.signIn(user.email, user.password, authStateObservable)
    }

    suspend fun register(
        user: UserEntity,
        authStateObservable: AuthorizationEventObservable
    ) {
        authService.signUp(user.email, user.password, authStateObservable)
    }

    suspend fun create(userStateObservable: UserStateObservable) {
        userService.create(userStateObservable)
    }

    suspend fun getTrendingMovies(requestedPage: Int): ShowListViewState =
        movieService.getTrending(requestedPage)


    suspend fun getTrendingTvShows(requestedPage: Int): ShowListViewState =
        tvShowService.getTrending(requestedPage)


    suspend fun getUpcomingMovies(requestedPage: Int): ShowListViewState =
        movieService.getUpcoming(requestedPage)


    suspend fun getUpcomingTvShows(requestedPage: Int): ShowListViewState =
        tvShowService.getUpcoming(requestedPage)


    suspend fun getGenres(): Response<AllGenresResponse> {
        return genreService.getAll()
    }

    suspend fun getMovieDetails(showId: Int): ShowViewState =
        movieService.getMovieDetails(showId)


    suspend fun getMovieCasts(showId: Int): CastsViewState =
        movieService.getMovieCasts(showId)


    suspend fun getTvShowDetails(showId: Int): ShowViewState =
        tvShowService.getDetails(showId)


    suspend fun getTvShowCasts(
        showId: Int
    ): CastsViewState =
        tvShowService.getCasts(showId)


    suspend fun getMoviesByGenre(id: String): Response<MoviesResponse> {
        return genreService.getMoviesByGenre(id)
    }

    suspend fun getRequestToken(
        authStateObservable: AuthorizationEventObservable
    ) {
        tmdbAuthService.getRequestToken(authStateObservable)
    }

    suspend fun createSession(authStateObservable: AuthorizationEventObservable) {
        tmdbAuthService.createSession(authStateObservable)
    }

    suspend fun getAccountDetails(authStateObservable: AuthorizationEventObservable) {
        tmdbAuthService.getAccountDetails(session, authStateObservable)
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

    fun setSession(createdSession: Session) {
        this.session.apply {
            accountId = createdSession.accountId
            id = createdSession.id
            expiresAt = createdSession.expiresAt
        }
    }

    fun getDetails(): String {
        return "SessionID: $session.id\n AccountID: ${session.accountId}"
    }

    suspend fun postMarkAsFavorite(
        requestBody: MarkAsFavoriteRequestBody
    ): DetailsViewEvent =
        favoritesService.postMarkAsFavorite(session, requestBody)


    suspend fun getPeekIsFavoriteMovie(
        movieId: Int
    ): DetailsViewEvent =
        favoritesService.getPeekIsFavoriteMovie(movieId, session)


    suspend fun getPeekIsFavoriteTvShow(
        showId: Int,
        viewEvent: MutableLiveData<Event<DetailsViewEvent>>
    ) {
        favoritesService.getPeekIsFavoriteTvShow(showId, session, viewEvent)
    }


    suspend fun getFavoriteMovieList(
        viewEvent: MutableLiveData<Event<FavoritesViewEvent>>
    ) {
        favoritesService.getFavoriteMovieList(session, viewEvent)
    }

    suspend fun getFavoriteTvShowList(viewEvent: MutableLiveData<Event<FavoritesViewEvent>>) {
        favoritesService.getFavoriteTvShowList(session, viewEvent)
    }

    suspend fun getMovieReviews(
        movieId: Int,
        viewEvent: MutableLiveData<Event<ReviewViewEvent>>
    ) {
        reviewService.getMovieReviews(movieId, viewEvent)
    }

    suspend fun getMovieTrailersInfo(
        movieId: Int
    ): DetailsViewEvent {
        return movieService.getMovieTrailersInfo(movieId)
    }

    suspend fun getTvShowTrailers(
        showId: Int
    ): DetailsViewEvent = tvShowService.getTvShowTrailers(showId)


}