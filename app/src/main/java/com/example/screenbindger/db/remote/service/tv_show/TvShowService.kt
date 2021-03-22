package com.example.screenbindger.db.remote.service.tv_show

import com.example.screenbindger.R
import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.model.domain.movie.generateGenres
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
import com.example.screenbindger.view.fragment.FetchedTvShows
import com.example.screenbindger.view.fragment.NotFetched
import com.example.screenbindger.view.fragment.details.*
import com.example.screenbindger.view.fragment.ShowListViewState
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class TvShowService @Inject constructor(
    private val api: TvShowApi
) {

    suspend fun getUpcoming(page: Int): ShowListViewState {
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

                    FetchedTvShows(
                        sortedListWithGeneratedGenres,
                        currentPage,
                        totalPages
                    )
                } else {
                    val message = response.getErrorResponse().statusMessage
                    NotFetched(Event(message))
                }
            }
        } catch (e: Exception) {
            NotFetched(Event("Network request fail"))
        }
    }

    // TODO: Implement handling request fail
    suspend fun getTrending(page: Int): ShowListViewState {
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

                    FetchedTvShows(
                        sortedListWithGeneratedGenres,
                        currentPage,
                        totalPages
                    )
                } else {
                    val message = response.getErrorResponse().statusMessage
                    NotFetched(Event(message))
                }
            }
        } catch (e: Exception) {
            NotFetched(Event(e.message!!))
        }
    }

    // TODO: Implement handling request fail
    suspend fun getDetails(
        showId: Int
    ): ShowViewState {
        return try {
            api.getDetails(showId).let { response ->
                if (response.isSuccessful) {
                    val showEntity = getShowEntityWithGenres(response)

                    if (showEntity != null) {
                        ShowViewState.Fetched(showEntity)
                    } else {
                        ShowViewState.NotFetched
                    }

                } else {
                    ShowViewState.NotFetched
                }
            }
        } catch (e: Exception) {
            ShowViewState.NotFetched
        }
    }

    private suspend fun getShowEntityWithGenres(response: Response<ShowEntity>): ShowEntity? =
        withContext(Default) {
            response.body()?.apply { generateGenreString() }
        }

    // TODO: Implement handling request fail
    suspend fun getCasts(
        showId: Int
    ): CastsViewState {
        return try {
            api.getCasts(showId).let { response ->
                if (response.isSuccessful) {
                    val list = response.body()?.casts

                    CastsViewState.Fetched(list ?: emptyList())
                } else {
                    CastsViewState.NotFetched
                }
            }
        } catch (e: Exception) {
            CastsViewState.NotFetched
        }
    }

    // TODO: Implement handling request fail
    suspend fun getTvShowTrailers(tvShowId: Int): DetailsViewEvent {
        return api.getTrailers(tvShowId).let { response ->
            if (response.isSuccessful) {
                val list = response.body()?.list ?: emptyList()
                if (list.isNotEmpty())
                    TrailersFetched(list)
            }
            TrailersNotFetched(R.string.trailers_not_fetched)
        }
    }
}