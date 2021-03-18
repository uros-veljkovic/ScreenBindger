package com.example.screenbindger.db.remote.service.movie

import com.example.screenbindger.model.domain.movie.generateGenres
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
import com.example.screenbindger.view.fragment.details.ShowDetailsState
import com.example.screenbindger.view.fragment.details.DetailsViewEvent
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewState
import com.example.screenbindger.view.fragment.trending.TrendingViewState
import com.example.screenbindger.view.fragment.upcoming.UpcomingViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO: Implement network request handling
class MovieService
@Inject
constructor(
    private val movieApi: MovieApi
) {

    suspend fun getTrending(page: Int): TrendingViewState {
        return try {

            movieApi.getTrendingMovies(page).let { response ->
                if (response.isSuccessful) {
                    val body = response.body()!!

                    val totalPages = body.totalPages
                    val currentPage = body.page

                    val listWithGeneratedGenres = body.list.generateGenres()

                    TrendingViewState.Fetched.Movies(
                        listWithGeneratedGenres,
                        currentPage,
                        totalPages
                    )
                } else {
                    val message = response.getErrorResponse().statusMessage
                    TrendingViewState.NotFetched(Event(message))
                }
            }
        } catch (e: Exception) {
            TrendingViewState.NotFetched(Event("Network request failed"))
        }
    }

    suspend fun getUpcoming(
        page: Int
    ): UpcomingViewState {
        return movieApi.getUpcomingMovies(page).let { response ->
            if (response.isSuccessful) {
                val body = response.body()!!

                val totalPages = body.totalPages
                val currentPage = body.page

                val list = body.list.generateGenres()

                UpcomingViewState.Fetched.Movies(list, currentPage, totalPages)
            } else {
                val message = response.getErrorResponse().statusMessage
                UpcomingViewState.NotFetched(Event(message))
            }
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

    suspend fun getMovieTrailersInfo(
        movieId: Int
    ): DetailsViewEvent {
        return movieApi.getMovieTrailers(movieId).let { response ->
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