package com.example.screenbindger.db.remote.service.tv_show

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.db.remote.request.MarkAsFavoriteRequestBody
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.domain.movie.generateGenres
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
import com.example.screenbindger.util.extensions.ifLet
import com.example.screenbindger.view.fragment.details.DetailsViewEvent
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewState
import com.example.screenbindger.view.fragment.details.ShowDetailsState
import com.example.screenbindger.view.fragment.trending.TrendingViewState
import com.example.screenbindger.view.fragment.upcoming.UpcomingViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TvShowService @Inject constructor(
    private val api: TvShowApi
) {
    // TODO : Test when network request fails
    suspend fun getUpcoming(page: Int): UpcomingViewState {
        return try {
            api.getUpcoming(page = page).let { response ->

                if (response.isSuccessful) {
                    val body = response.body()!!

                    val sortedListWithGeneratedGenres = withContext(Default) {
                        body.list?.apply {
                            sortedByDescending { it.rating }
                            generateGenres()
                        } ?: emptyList()
                    }

                    val currentPage = body.page
                    val totalPages = body.totalPages

                    UpcomingViewState.Fetched.TvShows(
                        sortedListWithGeneratedGenres,
                        currentPage,
                        totalPages
                    )
                } else {
                    val message = response.getErrorResponse().statusMessage
                    UpcomingViewState.NotFetched(Event(message))
                }
            }
        } catch (e: Exception) {
            UpcomingViewState.NotFetched(Event("Network request fail"))
        }
    }

    // TODO: Implement handling request fail
    suspend fun getTrending(page: Int): TrendingViewState {
        return try {
            api.getTrending(page = page).let { response ->

                if (response.isSuccessful) {
                    val body = response.body()!!

                    val sortedListWithGeneratedGenres = withContext(Default) {
                        body.list?.apply {
                            sortedByDescending { it.rating }
                            generateGenres()
                        } ?: emptyList()
                    }

                    val currentPage = body.page
                    val totalPages = body.totalPages

                    TrendingViewState.Fetched.TvShows(
                        sortedListWithGeneratedGenres,
                        currentPage,
                        totalPages
                    )
                } else {
                    val message = response.getErrorResponse().statusMessage
                    TrendingViewState.NotFetched(Event(message))
                }
            }
        } catch (e: Exception) {
            TrendingViewState.NotFetched(Event(e.message!!))
        }
    }

    // TODO: Implement handling request fail
    suspend fun getDetails(
        showId: Int,
        viewState: DetailsFragmentViewState
    ) {
        api.getDetails(showId).also { response ->
            if (response.isSuccessful) {
                viewState.apply {
                    show = response.body()
                    CoroutineScope(Default).launch {
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

    // TODO: Implement handling request fail
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

    // TODO: Implement handling request fail
    suspend fun getTvShowTrailers(tvShowId: Int): DetailsViewEvent {
        return api.getTrailers(tvShowId).let { response ->
            if (response.isSuccessful) {
                val list = response.body()?.list ?: emptyList()
                if (list.isNotEmpty())
                    DetailsViewEvent.TrailersFetched(list)
                else
                    DetailsViewEvent.TrailersNotFetched
            } else {
                DetailsViewEvent.TrailersNotFetched
            }
        }
    }
}