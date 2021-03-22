package com.example.screenbindger.view.fragment.trending

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.util.constants.POSITION_TAB_MOVIES
import com.example.screenbindger.util.constants.POSITION_TAB_TV_SHOWS
import com.example.screenbindger.view.fragment.upcoming.UpcomingViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrendingViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val viewState: MutableLiveData<TrendingViewState>
) : ViewModel() {

    var currentPage: Int = 1

    init {
        executeAction(TrendingViewAction.FetchMovies)
    }

    fun executeAction(action: TrendingViewAction) {
        when (action) {
            TrendingViewAction.FetchMovies -> {
                fetchMovies()
            }
            TrendingViewAction.FetchTvShows -> {
                fetchTvShows()
            }
            TrendingViewAction.GotoNextPage -> {
                nextPage()
            }
            TrendingViewAction.GotoPreviousPage -> {
                previousPage()
            }
        }
    }

    private fun nextPage() {
        currentPage++
        fetchBasedOnState()
    }

    private fun previousPage() {
        currentPage--
        fetchBasedOnState()
    }

    private fun fetchBasedOnState() {
        when (viewState.value) {
            is TrendingViewState.Fetched.Movies -> fetchMovies()
            is TrendingViewState.Fetched.TvShows -> fetchTvShows()
            else -> return
        }
    }

    private fun fetchMovies() = executeActionAndSetState {
        remoteDataSource.getTrendingMovies(currentPage)
    }

    private fun fetchTvShows() = executeActionAndSetState {
        remoteDataSource.getTrendingTvShows(currentPage)
    }


    fun getDirection(showId: Int): NavDirections? = when (viewState.value) {
        is TrendingViewState.Fetched.Movies -> {
            TrendingFragmentDirections.actionTrendingFragmentToMovieDetailsFragment(showId)
        }
        is TrendingViewState.Fetched.TvShows -> {
            TrendingFragmentDirections.actionTrendingFragmentToTvShowDetailsFragment(showId)
        }
        else -> {
            null
        }
    }

    fun tabSelected(position: Int) {
        when (position) {
            POSITION_TAB_MOVIES -> executeAction(TrendingViewAction.FetchMovies)
            POSITION_TAB_TV_SHOWS -> executeAction(TrendingViewAction.FetchTvShows)
        }
    }


    private fun executeActionAndSetState(actionReturningState: suspend () -> TrendingViewState) =
        viewModelScope.launch(IO) {
            val newState = actionReturningState()
            viewState.postValue(newState)
        }

}