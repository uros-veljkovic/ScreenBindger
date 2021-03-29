package com.example.screenbindger.view.fragment.trending

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.util.constants.POSITION_TAB_MOVIES
import com.example.screenbindger.util.constants.POSITION_TAB_TV_SHOWS
import com.example.screenbindger.view.fragment.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrendingViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val viewState: MutableLiveData<ShowListViewState>
) : ViewModel() {

    var currentPage: Int = 1

    fun executeAction(action: ShowListViewAction) {
        when (action) {
            is FetchMovies -> {
                fetchMovies()
            }
            is FetchTvShows -> {
                fetchTvShows()
            }
            is GotoNextPage -> {
                nextPage()
            }
            is GotoPreviousPage -> {
                previousPage()
            }
            is ResetState -> {
                viewState.value = ShowListViewState.Fetching
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
            is ShowListViewState.FetchedTvShows -> fetchTvShows()
            is ShowListViewState.Fetching,
            is ShowListViewState.FetchedMovies -> fetchMovies()
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
        is ShowListViewState.FetchedMovies -> {
            TrendingFragmentDirections.actionTrendingFragmentToMovieDetailsFragment(showId)
        }
        is ShowListViewState.FetchedTvShows -> {
            TrendingFragmentDirections.actionTrendingFragmentToTvShowDetailsFragment(showId)
        }
        else -> {
            null
        }
    }

    fun tabSelected(position: Int) {
        currentPage = 1
        when (position) {
            POSITION_TAB_MOVIES -> executeAction(FetchMovies)
            POSITION_TAB_TV_SHOWS -> executeAction(FetchTvShows)
        }
    }


    private fun executeActionAndSetState(actionReturningState: suspend () -> ShowListViewState) =
        viewModelScope.launch(IO) {
            val newState = actionReturningState()
            viewState.postValue(newState)
        }

}