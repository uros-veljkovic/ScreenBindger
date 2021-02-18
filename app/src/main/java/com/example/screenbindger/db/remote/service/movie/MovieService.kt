package com.example.screenbindger.db.remote.service.movie

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.db.remote.request.MarkAsFavoriteRequestBody
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.domain.cast.CastEntity
import com.example.screenbindger.model.domain.movie.MovieEntity
import com.example.screenbindger.model.global.Genres
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
import com.example.screenbindger.util.extensions.ifLet
import com.example.screenbindger.view.fragment.favorite_movies.FavoriteMoviesFragmentViewEvent
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsState
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsFragmentViewEvent
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsFragmentViewState
import com.example.screenbindger.view.fragment.trending.TrendingFragmentViewState
import com.example.screenbindger.view.fragment.upcoming.UpcomingFragmentViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieService
@Inject
constructor(
    private val movieApi: MovieApi
) {

    suspend fun getTrending(trendingViewState: MutableLiveData<TrendingFragmentViewState>) {
        movieApi.getTrendingMovies().let { response ->
            val list = response.body()?.list ?: emptyList()
            val state: TrendingFragmentViewState
            state = if (response.isSuccessful) {
                generateGenres(list)
                TrendingFragmentViewState(ListState.Fetched, list)
            } else {
                val message = response.getErrorResponse().statusMessage
                TrendingFragmentViewState(ListState.NotFetched(Event(message)), null)
            }
            trendingViewState.postValue(state)
        }
    }

    suspend fun getUpcoming(upcomingViewState: MutableLiveData<UpcomingFragmentViewState>) {
        movieApi.getUpcomingMovies().let { response ->
            val list = response.body()?.list ?: emptyList()
            val state: UpcomingFragmentViewState
            state = if (response.isSuccessful) {
                generateGenres(list)
                UpcomingFragmentViewState(ListState.Fetched, list)
            } else {
                val message = response.getErrorResponse().statusMessage
                UpcomingFragmentViewState(ListState.NotFetched(Event(message)), null)
            }
            upcomingViewState.postValue(state)
        }
    }

    private fun generateGenres(list: List<MovieEntity>) {
        CoroutineScope(Dispatchers.Default).launch {
            list.forEach { movie ->
                movie.genreIds?.forEach { generId ->
                    Genres.list.forEach { concreteGenre ->
                        if (concreteGenre.id == generId &&
                            movie.genresString.contains(concreteGenre.name!!).not()
                        ) {
                            movie.genresString += "${concreteGenre.name}, "
                        }
                    }
                }
                movie.genresString = movie.genresString.dropLast(2)
            }
        }
    }

    suspend fun getMovieDetails(
        movieId: Int,
        viewState: MutableLiveData<MovieDetailsFragmentViewState>
    ) {
        movieApi.getMovieDetails(movieId).let { response ->
            if (response.isSuccessful) {
                val movie: MovieEntity? = response.body()
                val state =
                    MovieDetailsFragmentViewState(
                        Event(MovieDetailsState.MovieFetched),
                        movie = movie
                    )
                viewState.postValue(state)
            } else {
                val message = "Error loading movie poster and description."
                val state =
                    MovieDetailsFragmentViewState(
                        Event(MovieDetailsState.Error.MovieNotFetched(message))
                    )
                viewState.postValue(state)
            }
        }
    }

    suspend fun getMovieCasts(
        movieId: Int,
        viewState: MutableLiveData<MovieDetailsFragmentViewState>
    ) {
        movieApi.getMovieCasts(movieId).let {
            if (it.isSuccessful) {
                val list: List<CastEntity>? = it.body()?.casts
                val state =
                    MovieDetailsFragmentViewState(
                        Event(MovieDetailsState.CastsFetched),
                        casts = list
                    )
                viewState.postValue(state)
            } else {
                val message = "Failed to load cast for the movie."
                val state = MovieDetailsFragmentViewState(
                    Event(MovieDetailsState.Error.CastsNotFetched(message))
                )
                viewState.postValue(state)
            }
        }
    }

    suspend fun postMovieAsFavorite(
        session: Session,
        body: MarkAsFavoriteRequestBody,
        viewEffect: MutableLiveData<Event<MovieDetailsFragmentViewEvent>>
    ) {
        ifLet(session.id, session.accountId) {
            movieApi.postMarkAsFavorite(
                sessionId = session.id!!,
                accountId = session.accountId!!,
                body = body
            ).let {
                if (it.isSuccessful) {
                    if (body.favorite)
                        viewEffect.postValue(Event(MovieDetailsFragmentViewEvent.AddedToFavorites()))
                    else
                        viewEffect.postValue(Event(MovieDetailsFragmentViewEvent.RemovedFromFavorites()))
                } else {
                    val error = it.getErrorResponse().statusMessage
                    viewEffect.postValue(Event(MovieDetailsFragmentViewEvent.Error(error)))
                }
            }
        }
    }

    suspend fun getIsMovieFavorite(
        movieId: Int,
        session: Session,
        viewEvent: MutableLiveData<Event<MovieDetailsFragmentViewEvent>>
    ) {
        ifLet(session.id, session.accountId) {
            movieApi.getFavoriteMovieList(
                sessionId = session.id!!,
                accountId = session.accountId!!
            ).let { response ->
                if (response.isSuccessful) {
                    response.body()?.list?.forEach { movie ->
                        if (movie.id!! == movieId) {
                            viewEvent.postValue(Event(MovieDetailsFragmentViewEvent.IsLoadedAsFavorite))
                            return
                        }
                    }
                    viewEvent.postValue(Event(MovieDetailsFragmentViewEvent.IsLoadedAsNotFavorite))
                } else {
                    viewEvent.postValue(
                        Event(
                            MovieDetailsFragmentViewEvent.Error(
                                "Error finding out if this is you favorite movie :("
                            )
                        )
                    )
                }
            }
        }
    }

    suspend fun getFavoriteMovieList(
        session: Session,
        viewEvent: MutableLiveData<Event<FavoriteMoviesFragmentViewEvent>>
    ) {
        movieApi.getFavoriteMovieList(
            sessionId = session.id!!,
            accountId = session.accountId!!
        ).let { response ->
            var message = ""
            if (response.isSuccessful) {
                val list = response.body()?.list
                if (list.isNullOrEmpty()) {
                    message = "No favorite movies added so far."
                    viewEvent.postValue(Event(FavoriteMoviesFragmentViewEvent.EmptyList(message)))
                } else {
                    generateGenres(list)
                    viewEvent.postValue(Event(FavoriteMoviesFragmentViewEvent.MoviesLoaded(list)))
                }
            } else {
                message = "Error loading favorite movies :("
                viewEvent.postValue(Event(FavoriteMoviesFragmentViewEvent.Error(message)))
            }
        }
    }

    suspend fun getMovieTrailersInfo(
        movieId: Int,
        viewEvent: MutableLiveData<Event<MovieDetailsFragmentViewEvent>>
    ) {
        movieApi.getMovieTrailers(movieId).let { response ->
            if (response.isSuccessful) {
                response.body()?.list?.let { list ->
                    if (list.isNotEmpty()) {
                        viewEvent.postValue(Event(MovieDetailsFragmentViewEvent.TrailersFetched(list)))
                    } else {
                        viewEvent.postValue(Event(MovieDetailsFragmentViewEvent.TrailersNotFetched))
                    }
                }
            } else {
                viewEvent.postValue(Event(MovieDetailsFragmentViewEvent.Error("Error loading trailer.")))
            }
        }
    }

}