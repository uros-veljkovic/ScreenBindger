package com.example.screenbindger.db.remote.service.movie

import com.example.screenbindger.R
import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.model.domain.movie.generateGenres
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
import com.example.screenbindger.view.fragment.FetchedMovies
import com.example.screenbindger.view.fragment.NotFetched
import com.example.screenbindger.view.fragment.details.*
import com.example.screenbindger.view.fragment.ShowListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

//TODO: Implement network request handling
class MovieService
@Inject
constructor(
    private val movieApi: MovieApi
) {

    suspend fun getTrending(page: Int): ShowListViewState {
        return try {

            movieApi.getTrendingMovies(page).let { response ->
                if (response.isSuccessful) {
                    val body = response.body()!!

                    val totalPages = body.totalPages
                    val currentPage = body.page

                    val listWithGeneratedGenres = body.list.generateGenres()
                    FetchedMovies(
                        listWithGeneratedGenres,
                        currentPage,
                        totalPages
                    )
                } else {
                    val message = response.getErrorResponse().statusMessage
                    NotFetched(Event(message))
                }
            }
        } catch (e: Exception) {
            NotFetched(Event("Network request failed"))
        }
    }

    suspend fun getUpcoming(
        page: Int
    ): ShowListViewState {
        return movieApi.getUpcomingMovies(page).let { response ->
            if (response.isSuccessful) {
                val body = response.body()!!

                val totalPages = body.totalPages
                val currentPage = body.page

                val list = body.list.generateGenres()

                FetchedMovies(
                    list,
                    currentPage,
                    totalPages
                )
            } else {
                val message = response.getErrorResponse().statusMessage
                NotFetched(Event(message))
            }
        }
    }

    suspend fun getMovieDetails(
        movieId: Int
    ): ShowViewState {
        return try {
            movieApi.getMovieDetails(movieId).let { response ->
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
        withContext(Dispatchers.Default) {
            response.body()?.apply { generateGenreString() }
        }

    suspend fun getMovieCasts(
        movieId: Int
    ): CastsViewState {
        return try {
            movieApi.getMovieCasts(movieId).let { response ->
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

    suspend fun getMovieTrailersInfo(
        movieId: Int
    ): DetailsViewEvent {
        return movieApi.getMovieTrailers(movieId).let { response ->
            if (response.isSuccessful) {
                val list = response.body()?.list ?: emptyList()
                if (list.isNotEmpty())
                    TrailersFetched(list)
            }
            TrailersNotFetched(R.string.trailers_not_fetched)
        }
    }


}