package com.example.screenbindger.view.fragment.upcoming

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val viewState: MutableLiveData<UpcomingViewState>,
    val viewAction: MutableLiveData<UpcomingViewAction>
) : ViewModel() {

    var currentPage: Int = 1

    fun fetchMovies() = viewModelScope.launch(IO) {
        val newState = remoteDataSource.getUpcomingMovies(currentPage)
        setState(newState)
    }

    fun fetchTvShows() = viewModelScope.launch(IO) {
        val newState = remoteDataSource.getUpcomingTvShows(currentPage)
        setState(newState)
    }

    private fun setState(newState: UpcomingViewState) {
        viewState.postValue(newState)
    }

    fun setAction(action: UpcomingViewAction) {
        viewAction.postValue(action)
    }

    fun getState(): UpcomingViewState = viewState.value!!

    fun nextPage() {
        currentPage++
        fetchAccordingToState()
    }

    fun previousPage() {
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

}

