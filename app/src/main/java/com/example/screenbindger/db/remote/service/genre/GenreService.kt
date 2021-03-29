package com.example.screenbindger.db.remote.service.genre

import com.example.screenbindger.db.remote.response.movie.MoviesByGenreResponse
import com.example.screenbindger.db.remote.response.genre.AllGenresResponse
import com.example.screenbindger.db.remote.response.movie.MoviesResponse
import com.example.screenbindger.model.domain.movie.generateGenres
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
import com.example.screenbindger.view.fragment.ShowListViewState
import retrofit2.Response
import java.lang.Exception

class GenreService
constructor(
    private val genreApi: GenreApi
) {

    suspend fun getAll(): Response<AllGenresResponse> {
        return genreApi.getAllGenres()
    }

    suspend fun getMoviesByGenre(id: String): ShowListViewState {
        return genreApi.getMoviesByGenre(id).let { response ->
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

    suspend fun getTvShowsByGenre(id: String): ShowListViewState {
        return genreApi.getTvShowsByGenre(id).let { response ->
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