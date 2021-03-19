package com.example.screenbindger.view.fragment.upcoming

import androidx.lifecycle.*
import androidx.navigation.NavDirections
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.view.fragment.trending.TrendingFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpcomingViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val viewState: MutableLiveData<UpcomingViewState>
) : ViewModel() {

    var currentPage: Int = 1

    fun executeAction(action: UpcomingViewAction) {
        when (action) {
            is UpcomingViewAction.FetchMovies -> {
                fetchMovies()
            }
            is UpcomingViewAction.FetchTvShows -> {
                fetchTvShows()
            }
            is UpcomingViewAction.GotoNextPage -> {
                nextPage()
            }
            is UpcomingViewAction.GotoPreviousPage -> {
                previousPage()
            }
        }
    }

    private fun fetchMovies() = executeActionAndSetState {
        remoteDataSource.getUpcomingMovies(currentPage)
    }

    private fun fetchTvShows() = executeActionAndSetState {
        remoteDataSource.getUpcomingTvShows(currentPage)
    }

    private fun nextPage() {
        currentPage++
        fetchAccordingToState()
    }

    private fun previousPage() {
        currentPage--
        fetchAccordingToState()
    }

    private fun fetchAccordingToState() {
        when (viewState.value) {
            is UpcomingViewState.Fetched.Movies -> fetchTvShows()
            is UpcomingViewState.Fetched.TvShows -> fetchMovies()
            else -> return
        }
    }

    fun getNavDirection(showId: Int): NavDirections? = when (viewState.value) {
        is UpcomingViewState.Fetched.Movies -> {
            TrendingFragmentDirections.actionTrendingFragmentToMovieDetailsFragment(showId)
        }
        is UpcomingViewState.Fetched.TvShows -> {
            TrendingFragmentDirections.actionTrendingFragmentToTvShowDetailsFragment(showId)
        }
        else -> {
            null
        }
    }

    private fun executeActionAndSetState(actionReturningState: suspend () -> UpcomingViewState) =
        viewModelScope.launch(IO) {
            val newState = actionReturningState()
            viewState.postValue(newState)
        }


}

