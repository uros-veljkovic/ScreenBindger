package com.example.screenbindger.db.remote.service.genre

import com.example.screenbindger.R
import com.example.screenbindger.model.domain.movie.generateGenres
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
import com.example.screenbindger.view.fragment.ShowListViewState
import com.example.screenbindger.view.fragment.genres.GenresViewState

class GenreService
constructor(
    private val genreApi: GenreApi
) {

    suspend fun getAll(): GenresViewState {
        return try {
            genreApi.getAllGenres().let { response ->
                if (response.isSuccessful) {
                    val list = response.body()?.list

                    if (list.isNullOrEmpty().not()) {
                        GenresViewState.Fetched(list!!)
                    } else {
                        GenresViewState.NotFetched(R.string.data_not_fetched)
                    }

                } else {
                    GenresViewState.NotFetched(R.string.data_not_fetched)
                }
            }
        } catch (e: Exception) {
            GenresViewState.NotFetched(R.string.network_error)
        }
    }

    suspend fun getMoviesByGenre(id: String, page: Int): ShowListViewState {
        return genreApi.getMoviesByGenre(id, page).let { response ->
            if (response.isSuccessful) {
                val body = response.body()!!

                val totalPages = body.totalPages
                val currentPage = body.page

                val list = body.list.generateGenres()

                ShowListViewState.FetchedMovies(
                    list,
                    currentPage,
                    totalPages
                )
            } else {
                val message = response.getErrorResponse().statusMessage
                ShowListViewState.NotFetched(Event(message))
            }
        }
    }

    suspend fun getTvShowsByGenre(id: String, page: Int): ShowListViewState {
        return genreApi.getTvShowsByGenre(id, page).let { response ->
            if (response.isSuccessful) {
                val body = response.body()!!

                val totalPages = body.totalPages
                val currentPage = body.page

                val list = body.list.generateGenres()

                ShowListViewState.FetchedTvShows(
                    list,
                    currentPage,
                    totalPages
                )
            } else {
                val message = response.getErrorResponse().statusMessage
                ShowListViewState.NotFetched(Event(message))
            }
        }
    }
}