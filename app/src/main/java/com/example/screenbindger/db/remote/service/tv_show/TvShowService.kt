package com.example.screenbindger.db.remote.service.tv_show

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.movie.MovieEntity
import com.example.screenbindger.model.global.Genres
import com.example.screenbindger.model.state.ListState
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
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
                val list = response.body()?.list ?: emptyList()

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

    private fun generateGenres(list: List<MovieEntity>) {
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
}