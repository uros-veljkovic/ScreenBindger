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
import com.example.screenbindger.view.fragment.favorite_movies.FavoritesViewEvent
import com.example.screenbindger.view.fragment.login.AuthorizationEventObservable
import com.example.screenbindger.view.fragment.details.DetailsViewEvent
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewState
import com.example.screenbindger.view.fragment.review.ReviewViewEvent
import com.example.screenbindger.view.fragment.trending.TrendingViewState
import com.example.screenbindger.view.fragment.upcoming.UpcomingViewState
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

    suspend fun getTrendingMovies(
        requestedPage: Int,
        viewState: MutableLiveData<TrendingViewState>
    ) {
        movieService.getTrending(requestedPage, viewState)
    }

    suspend fun getTrendingTvShows(
        requestedPage: Int,
        viewState: MutableLiveData<TrendingViewState>
    ) {
        tvShowService.getTrending(requestedPage, viewState)
    }

    suspend fun getUpcomingMovies(
        requestedPage: Int,
        viewState: MutableLiveData<UpcomingViewState>
    ) {
        movieService.getUpcoming(requestedPage, viewState)
    }

    suspend fun getUpcomingTvShows(
        requestedPage: Int,
        viewState: MutableLiveData<UpcomingViewState>
    ) {
        tvShowService.getUpcoming(requestedPage, viewState)
    }

    suspend fun getGenres(): Response<AllGenresResponse> {
        return genreService.getAll()
    }

    suspend fun getMovieDetails(
        showId: Int,
        viewState: DetailsFragmentViewState
    ) {
        movieService.getMovieDetails(showId, viewState)
    }

    suspend fun getMovieCasts(
        showId: Int,
        viewState: DetailsFragmentViewState
    ) {
        movieService.getMovieCasts(showId, viewState)
    }

    suspend fun getTvShowDetails(
        showId: Int,
        viewState: DetailsFragmentViewState
    ) {
        tvShowService.getDetails(showId, viewState)
    }

    suspend fun getTvShowCasts(
        showId: Int,
        viewState: DetailsFragmentViewState
    ) {
        tvShowService.getCasts(showId, viewState)
    }

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
        requestBody: MarkAsFavoriteRequestBody,
        viewEvent: MutableLiveData<Event<DetailsViewEvent>>
    ) {
        favoritesService.postMarkAsFavorite(session, requestBody, viewEvent)
    }

    suspend fun getPeekIsFavoriteMovie(
        movieId: Int,
        viewEvent: MutableLiveData<Event<DetailsViewEvent>>
    ) {
        favoritesService.getPeekIsFavoriteMovie(movieId, session, viewEvent)
    }

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
        movieId: Int,
        viewEvent: MutableLiveData<Event<DetailsViewEvent>>
    ) {
        movieService.getMovieTrailersInfo(movieId, viewEvent)
    }

    suspend fun getTvShowTrailers(
        showId: Int,
        viewEvent: MutableLiveData<Event<DetailsViewEvent>>
    ) {
        tvShowService.getTvShowTrailers(showId, viewEvent)
    }


}