package com.example.screenbindger.db.remote.service.tv_show

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.model.global.Genres
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewEvent
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewState
import com.example.screenbindger.view.fragment.details.ShowDetailsState
import com.example.screenbindger.view.fragment.trending.TrendingFragmentViewState
import com.example.screenbindger.view.fragment.upcoming.UpcomingFragmentViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TvShowService @Inject constructor(
    private val api: TvShowApi
) {
    suspend fun getUpcoming(viewState: MutableLiveData<UpcomingFragmentViewState>) {
        api.getUpcoming().let { response ->
            var state: UpcomingFragmentViewState? = null
            if (response.isSuccessful) {
                val list = response.body()?.list?.sortedByDescending { it.rating } ?: emptyList()

                state = if (response.isSuccessful) {
                    generateGenres(list)
                    UpcomingFragmentViewState(ListState.Fetched, list)
                } else {
                    val message = response.getErrorResponse().statusMessage
                    UpcomingFragmentViewState(ListState.NotFetched(Event(message)), null)
                }
            } else {
                val message = response.getErrorResponse().statusMessage
                UpcomingFragmentViewState(ListState.NotFetched(Event(message)), null)
            }
            viewState.postValue(state)
        }
    }

    suspend fun getTrending(viewState: MutableLiveData<TrendingFragmentViewState>) {
        api.getTrending().let { response ->
            var state: TrendingFragmentViewState? = null
            if (response.isSuccessful) {
                val list = response.body()?.list?.sortedByDescending { it.rating } ?: emptyList()

                state = if (response.isSuccessful) {
                    generateGenres(list)
                    TrendingFragmentViewState(ListState.Fetched, list)
                } else {
                    val message = response.getErrorResponse().statusMessage
                    TrendingFragmentViewState(ListState.NotFetched(Event(message)), null)
                }
            } else {
                val message = response.getErrorResponse().statusMessage
                TrendingFragmentViewState(ListState.NotFetched(Event(message)), null)
            }
            viewState.postValue(state)
        }
    }

    private fun generateGenres(list: List<ShowEntity>) {
        CoroutineScope(Dispatchers.Default).launch {
            list.forEach { tvShow ->
                tvShow.genreIds?.forEach { generId ->
                    Genres.list.forEach { concreteGenre ->
                        if (concreteGenre.id == generId &&
                            tvShow.genresString.contains(concreteGenre.name!!).not()
                        ) {
                            tvShow.genresString += "${concreteGenre.name}, "
                        }
                    }
                }
                tvShow.genresString = tvShow.genresString.dropLast(2)
            }
        }
    }

    suspend fun getDetails(
        showId: Int,
        viewState: DetailsFragmentViewState
    ) {
        api.getDetails(showId).also { response ->
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

    suspend fun getCasts(
        showId: Int,
        viewState: DetailsFragmentViewState
    ) {
        api.getCasts(showId).also { response ->
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

    suspend fun getTvShowTrailers(
        tvShowId: Int,
        viewEvent: MutableLiveData<Event<DetailsFragmentViewEvent>>
    ) {
        api.getTrailers(tvShowId).let { response ->
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