package com.example.screenbindger.db.remote.service.movie

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.db.remote.request.FavoriteMovieRequestBody
import com.example.screenbindger.db.remote.response.*
import com.example.screenbindger.model.domain.CastEntity
import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.model.global.Genres
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsState
import com.example.screenbindger.view.fragment.movie_details.MovieDetailsViewState
import com.example.screenbindger.view.fragment.trending.TrendingViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class MovieService
@Inject
constructor(
    private val movieApi: MovieApi
) {

    suspend fun getTrending(trendingViewState: MutableLiveData<TrendingViewState>) {
        movieApi.getTrending().let { response ->
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

    suspend fun getUpcoming(): Response<UpcomingMoviesResponse> {
        return movieApi.getUpcoming()
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
        sessionId: String,
        favoriteMovieRequestBody: FavoriteMovieRequestBody
    ): Response<FavoriteMovieResponse> {
        return movieApi.postMovieAsFavorite(
            sessionId = sessionId,
            favoriteMovieRequestBody = favoriteMovieRequestBody
        )
    }

}