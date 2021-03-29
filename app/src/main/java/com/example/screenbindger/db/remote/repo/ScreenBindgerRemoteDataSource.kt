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
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
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
    ) = withContext(IO) {
        authService.signIn(user.email, user.password, authStateObservable)
    }

    suspend fun register(
        user: UserEntity,
        authStateObservable: AuthorizationEventObservable
    ) = withContext(IO) {
        authService.signUp(user.email, user.password, authStateObservable)
    }

    suspend fun create(userStateObservable: UserStateObservable) = withContext(IO) {
        userService.create(userStateObservable)
    }

    suspend fun getTrendingMovies(requestedPage: Int): ShowListViewState = withContext(IO) {
        movieService.getTrending(requestedPage)
    }


    suspend fun getTrendingTvShows(requestedPage: Int): ShowListViewState = withContext(IO) {
        tvShowService.getTrending(requestedPage)
    }


    suspend fun getUpcomingMovies(requestedPage: Int): ShowListViewState = withContext(IO) {
        movieService.getUpcoming(requestedPage)
    }


    suspend fun getUpcomingTvShows(requestedPage: Int): ShowListViewState = withContext(IO) {
        tvShowService.getUpcoming(requestedPage)
    }

    suspend fun getGenres(): Response<AllGenresResponse> = withContext(IO) {
        genreService.getAll()
    }

    suspend fun getMovieDetails(showId: Int): ShowViewState = withContext(IO) {
        movieService.getMovieDetails(showId)
    }


    suspend fun getMovieCasts(showId: Int): CastsViewState = withContext(IO) {
        movieService.getMovieCasts(showId)
    }


    suspend fun getTvShowDetails(showId: Int): ShowViewState =
        tvShowService.getDetails(showId)


    suspend fun getTvShowCasts(
        showId: Int
    ): CastsViewState =
        tvShowService.getCasts(showId)


    suspend fun getMoviesByGenre(id: String): ShowListViewState = withContext(IO) {
        genreService.getMoviesByGenre(id)
    }

    suspend fun getTvShowsByGenre(id: String): ShowListViewState = withContext(IO) {
        genreService.getTvShowsByGenre(id)
    }

    suspend fun getRequestToken(
        authStateObservable: AuthorizationEventObservable
    ) = withContext(IO) {
        tmdbAuthService.getRequestToken(authStateObservable)
    }

    suspend fun createSession(
        authStateObservable: AuthorizationEventObservable
    ) = withContext(IO)
    {
        tmdbAuthService.createSession(authStateObservable)
    }

    suspend fun getAccountDetails(
        authStateObservable: AuthorizationEventObservable
    ) = withContext(IO) {
        tmdbAuthService.getAccountDetails(session, authStateObservable)
    }

    suspend fun changePassword(
        newPassword: String,
        userStateObservable: UserStateObservable
    ) = withContext(IO) {
        authService.changePassword(newPassword, userStateObservable)
    }

    suspend fun fetchUser(userStateObservable: UserStateObservable) = withContext(IO) {
        userService.read(userStateObservable)
    }

    suspend fun updateUser(userStateObservable: UserStateObservable) = withContext(IO) {
        userService.update(userStateObservable)
    }

    suspend fun uploadImage(uri: Uri, userStateObservable: UserStateObservable) = withContext(IO) {
        storageService.uploadImage(uri, userStateObservable)
    }

    suspend fun fetchProfilePicture(userStateObservable: UserStateObservable) = withContext(IO) {
        storageService.downloadImage(userStateObservable)
    }

    fun setSession(createdSession: Session) {
        this.session.apply {
            accountId = createdSession.accountId
            id = createdSession.id
            expiresAt = createdSession.expiresAt
        }
    }

    suspend fun postMarkAsFavorite(
        requestBody: MarkAsFavoriteRequestBody
    ): DetailsViewEvent = withContext(IO) {
        favoritesService.postMarkAsFavorite(session, requestBody)
    }


    suspend fun getPeekIsFavoriteMovie(movieId: Int): DetailsViewEvent = withContext(IO) {
        favoritesService.getPeekIsFavoriteMovie(movieId, session)
    }


    suspend fun getPeekIsFavoriteTvShow(showId: Int): DetailsViewEvent = withContext(IO) {
        favoritesService.getPeekIsFavoriteTvShow(showId, session)
    }

    suspend fun getFavoriteMovieList(
        viewEvent: MutableLiveData<Event<FavoritesViewEvent>>
    ) = withContext(IO) {
        favoritesService.getFavoriteMovieList(session, viewEvent)
    }

    suspend fun getFavoriteTvShowList(
        viewEvent: MutableLiveData<Event<FavoritesViewEvent>>
    ) = withContext(IO) {
        favoritesService.getFavoriteTvShowList(session, viewEvent)
    }

    suspend fun getMovieReviews(
        movieId: Int,
        viewEvent: MutableLiveData<Event<ReviewViewEvent>>
    ) {
        reviewService.getMovieReviews(movieId, viewEvent)
    }

    suspend fun getMovieTrailersInfo(movieId: Int): DetailsViewEvent = withContext(IO) {
        movieService.getMovieTrailersInfo(movieId)
    }

    suspend fun getTvShowTrailers(showId: Int): DetailsViewEvent = withContext(IO) {
        tvShowService.getTvShowTrailers(showId)
    }


}