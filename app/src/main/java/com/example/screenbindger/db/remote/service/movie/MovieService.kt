package com.example.screenbindger.db.remote.service.movie

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.db.remote.request.MarkAsFavoriteRequestBody
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.domain.movie.generateGenres
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
import com.example.screenbindger.util.extensions.ifLet
import com.example.screenbindger.view.fragment.favorite_movies.FavoritesViewEvent
import com.example.screenbindger.view.fragment.details.ShowDetailsState
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewEvent
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewState
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
            val list = response.body()?.list?.sortedByDescending { it.rating } ?: emptyList()
            val state: TrendingFragmentViewState
            state = if (response.isSuccessful) {
                list.generateGenres()
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
            val list = response.body()?.list?.sortedByDescending { it.rating } ?: emptyList()
            val state: UpcomingFragmentViewState
            state = if (response.isSuccessful) {
                list.generateGenres()
                UpcomingFragmentViewState(ListState.Fetched, list)
            } else {
                val message = response.getErrorResponse().statusMessage
                UpcomingFragmentViewState(ListState.NotFetched(Event(message)), null)
            }
            upcomingViewState.postValue(state)
        }
    }

    suspend fun getMovieDetails(
        movieId: Int,
        viewState: DetailsFragmentViewState
    ) {
        movieApi.getMovieDetails(movieId).also { response ->
            if (response.isSuccessful) {
                viewState.apply {
                    show = response.body()
                    CoroutineScope(Dispatchers.Default).launch {
                        show?.generateGenreString()
                    }
                }

                val isCastsProcessedOrNotFetched =
                    (viewState.currentState() is ShowDetailsState.CastsProcessed)
                        .or(viewState.currentState() is ShowDetailsState.Error.CastsNotFetched)

                if (isCastsProcessedOrNotFetched) {
                    viewState.prepareForFinalState()
                } else {
                    viewState.setState(ShowDetailsState.ShowProcessed)
                }
            } else {
                val message = response.getErrorResponse().statusMessage
                viewState.setState(ShowDetailsState.Error.ShowNotFetched(message))
            }
        }
    }

    suspend fun getMovieCasts(
        movieId: Int,
        viewState: DetailsFragmentViewState
    ) {
        movieApi.getMovieCasts(movieId).also { response ->
            if (response.isSuccessful) {
                viewState.casts = response.body()?.casts

                val movieProcessedOrNotFetched: Boolean =
                    (viewState.currentState() is ShowDetailsState.ShowProcessed)
                        .or(viewState.currentState() is ShowDetailsState.Error.ShowNotFetched)


                if (movieProcessedOrNotFetched) {
                    viewState.prepareForFinalState()
                } else {
                    viewState.setState(ShowDetailsState.CastsProcessed)
                }

            } else {
                val message = response.getErrorResponse().statusMessage
                viewState.setState(ShowDetailsState.Error.CastsNotFetched(message))
            }
        }
    }

    suspend fun postMovieAsFavorite(
        session: Session,
        body: MarkAsFavoriteRequestBody,
        viewEffect: MutableLiveData<Event<DetailsFragmentViewEvent>>
    ) {
        ifLet(session.id, session.accountId) {
            movieApi.postMarkAsFavorite(
                sessionId = session.id!!,
                accountId = session.accountId!!,
                body = body
            ).let {
                if (it.isSuccessful) {
                    if (body.favorite)
                        viewEffect.postValue(Event(DetailsFragmentViewEvent.AddedToFavorites()))
                    else
                        viewEffect.postValue(Event(DetailsFragmentViewEvent.RemovedFromFavorites()))
                } else {
                    val error = it.getErrorResponse().statusMessage
                    viewEffect.postValue(Event(DetailsFragmentViewEvent.Error(error)))
                }
            }
        }
    }

    suspend fun getIsMovieFavorite(
        movieId: Int,
        session: Session,
        viewEvent: MutableLiveData<Event<DetailsFragmentViewEvent>>
    ) {
        ifLet(session.id, session.accountId) {
            movieApi.getFavoriteMovieList(
                sessionId = session.id!!,
                accountId = session.accountId!!
            ).let { response ->
                if (response.isSuccessful) {
                    response.body()?.list?.forEach { movie ->
                        if (movie.id!! == movieId) {
                            viewEvent.postValue(Event(DetailsFragmentViewEvent.IsLoadedAsFavorite))
                            return
                        }
                    }
                    viewEvent.postValue(Event(DetailsFragmentViewEvent.IsLoadedAsNotFavorite))
                } else {
                    viewEvent.postValue(
                        Event(
                            DetailsFragmentViewEvent.Error(
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
        viewEvent: MutableLiveData<Event<FavoritesViewEvent>>
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
                    viewEvent.postValue(Event(FavoritesViewEvent.EmptyList(message)))
                } else {
                    list.generateGenres()
                    viewEvent.postValue(Event(FavoritesViewEvent.ListLoaded(list)))
                }
            } else {
                message = "Error loading favorite movies :("
                viewEvent.postValue(Event(FavoritesViewEvent.Error(message)))
            }
        }
    }

    suspend fun getMovieTrailersInfo(
        movieId: Int,
        viewEvent: MutableLiveData<Event<DetailsFragmentViewEvent>>
    ) {
        movieApi.getMovieTrailers(movieId).let { response ->
            if (response.isSuccessful) {
                response.body()?.list?.let { list ->
                    if (list.isNotEmpty()) {
                        viewEvent.postValue(Event(DetailsFragmentViewEvent.TrailersFetched(list)))
                    } else {
                        viewEvent.postValue(Event(DetailsFragmentViewEvent.TrailersNotFetched))
                    }
                }
            } else {
                viewEvent.postValue(Event(DetailsFragmentViewEvent.TrailersNotFetched))
            }
        }
    }

}