package com.example.screenbindger.db.remote.service.movie

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.db.remote.request.MarkAsFavoriteRequestBody
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.domain.CastEntity
import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.model.global.Genres
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
import com.example.screenbindger.util.extensions.ifLet
import com.example.screenbindger.view.fragment.favorite_movies.FavoriteMoviesViewEvent
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsState
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsViewEvent
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsViewState
import com.example.screenbindger.view.fragment.trending.TrendingViewState
import com.example.screenbindger.view.fragment.upcoming.UpcomingViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieService
@Inject
constructor(
    private val movieApi: MovieApi
) {

    suspend fun getTrending(trendingViewState: MutableLiveData<TrendingViewState>) {
        movieApi.getTrendingMovies().let { response ->
            val list = response.body()?.list ?: emptyList()
            if (response.isSuccessful) {
                generateGenres(list)
                val state = TrendingViewState(ListState.Fetched, list)
                trendingViewState.postValue(state)
            } else {
                val message = response.getErrorResponse().statusMessage
                val state = TrendingViewState(ListState.NotFetched(Event(message)), null)
                trendingViewState.postValue(state)
            }
        }
    }

    suspend fun getUpcoming(upcomingViewState: MutableLiveData<UpcomingViewState>) {
        movieApi.getUpcomingMovies().let { response ->
            val list = response.body()?.list ?: emptyList()
            if (response.isSuccessful) {
                generateGenres(list)
                val state = UpcomingViewState(ListState.Fetched, list)
                upcomingViewState.postValue(state)
            } else {
                val message = response.getErrorResponse().statusMessage
                val state = UpcomingViewState(ListState.NotFetched(Event(message)), null)
                upcomingViewState.postValue(state)
            }
        }
    }

    private fun generateGenres(list: List<MovieEntity>) {
        CoroutineScope(Dispatchers.Default).launch {
            list.forEach { item ->
                item.genreIds?.forEach { generId ->
                    Genres.list.forEach {
                        if (it.id == generId) {
                            item.genresString += "${it.name}, "
                        }
                    }
                }
                item.genresString = item.genresString.dropLast(2)
            }
        }
    }

    suspend fun getMovieDetails(
        movieId: Int,
        viewState: MutableLiveData<MovieDetailsViewState>
    ) {
        movieApi.getMovieDetails(movieId).let { response ->
            if (response.isSuccessful) {
                val movie: MovieEntity? = response.body()
                val state =
                    MovieDetailsViewState(Event(MovieDetailsState.MovieFetched), movie = movie)
                viewState.postValue(state)
            } else {
                val message = "Error loading movie poster and description."
                val state =
                    MovieDetailsViewState(
                        Event(MovieDetailsState.Error.MovieNotFetched(message))
                    )
                viewState.postValue(state)
            }
        }
    }

    suspend fun getMovieCasts(
        movieId: Int,
        viewState: MutableLiveData<MovieDetailsViewState>
    ) {
        movieApi.getMovieCasts(movieId).let {
            if (it.isSuccessful) {
                val list: List<CastEntity>? = it.body()?.casts
                val state =
                    MovieDetailsViewState(Event(MovieDetailsState.CastsFetched), casts = list)
                viewState.postValue(state)
            } else {
                val message = "Failed to load cast for the movie."
                val state = MovieDetailsViewState(
                    Event(MovieDetailsState.Error.CastsNotFetched(message))
                )
                viewState.postValue(state)
            }
        }
    }

    suspend fun postMovieAsFavorite(
        session: Session,
        body: MarkAsFavoriteRequestBody,
        viewEffect: MutableLiveData<Event<MovieDetailsViewEvent>>
    ) {
        ifLet(session.id, session.accountId) {
            movieApi.postMarkAsFavorite(
                sessionId = session.id!!,
                accountId = session.accountId!!,
                body = body
            ).let {
                if (it.isSuccessful) {
                    if (body.favorite)
                        viewEffect.postValue(Event(MovieDetailsViewEvent.AddedToFavorites()))
                    else
                        viewEffect.postValue(Event(MovieDetailsViewEvent.RemovedFromFavorites()))
                } else {
                    val error = it.getErrorResponse().statusMessage
                    viewEffect.postValue(Event(MovieDetailsViewEvent.Error(error)))
                }
            }
        }
    }

    suspend fun getIsMovieFavorite(
        movieId: Int,
        session: Session,
        viewEvent: MutableLiveData<Event<MovieDetailsViewEvent>>
    ) {
        ifLet(session.id, session.accountId) {
            movieApi.getFavoriteMovieList(
                sessionId = session.id!!,
                accountId = session.accountId!!
            ).let { response ->
                if (response.isSuccessful) {
                    response.body()?.list?.forEach { movie ->
                        if (movie.id!! == movieId) {
                            viewEvent.postValue(Event(MovieDetailsViewEvent.IsLoadedAsFavorite))
                            return
                        }
                    }
                    viewEvent.postValue(Event(MovieDetailsViewEvent.IsLoadedAsNotFavorite))
                } else {
                    viewEvent.postValue(
                        Event(
                            MovieDetailsViewEvent.Error(
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
        viewEvent: MutableLiveData<Event<FavoriteMoviesViewEvent>>
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
                    viewEvent.postValue(Event(FavoriteMoviesViewEvent.EmptyList(message)))
                } else
                    viewEvent.postValue(Event(FavoriteMoviesViewEvent.MoviesLoaded(list)))
            } else {
                message = "Error loading favorite movies :("
                viewEvent.postValue(Event(FavoriteMoviesViewEvent.Error(message)))
            }
        }
    }

}