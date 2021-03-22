package com.example.screenbindger.view.fragment.upcoming

import androidx.lifecycle.*
import androidx.navigation.NavDirections
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.view.fragment.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpcomingViewModel
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
                viewState.value = Fetching
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
            is FetchedTvShows -> fetchTvShows()
            is FetchedMovies -> fetchMovies()
            else -> return
        }
    }

    fun getNavDirection(showId: Int): NavDirections? = when (viewState.value) {
        is FetchedMovies -> {
            UpcomingFragmentDirections.actionUpcomingFragmentToMovieDetailsFragment(showId)
        }
        is FetchedTvShows -> {
            UpcomingFragmentDirections.actionUpcomingFragmentToTvShowDetailsFragment(showId)
        }
        else -> {
            null
        }
    }

    private fun executeActionAndSetState(actionReturningState: suspend () -> ShowListViewState) =
        viewModelScope.launch(IO) {
            val newState = actionReturningState()
            viewState.postValue(newState)
        }


}

