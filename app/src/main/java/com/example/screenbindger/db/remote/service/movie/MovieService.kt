package com.example.screenbindger.db.remote.service.movie

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.db.remote.request.FavoriteMovieRequestBody
import com.example.screenbindger.db.remote.response.*
import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.model.global.Genres
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
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

    suspend fun getMovieDetails(movieId: Int): Response<MovieEntity> {
        return movieApi.getMovieDetails(movieId)
    }

    suspend fun getMovieCasts(movieId: Int): Response<MovieDetailsCastResponse> {
        return movieApi.getMovieCasts(movieId)
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