package com.example.screenbindger.view.fragment.upcoming

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.view.fragment.trending.TrendingFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpcomingViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val viewState: MutableLiveData<UpcomingViewState>,
    val viewAction: MutableLiveData<UpcomingViewAction>,
    private val coroutineIo: CoroutineScope
) : ViewModel() {

    var currentPage: Int = 1

    fun fetchMovies() = coroutineIo.launch {
        remoteDataSource.getUpcomingMovies(currentPage, viewState)
    }

    fun fetchTvShows() = coroutineIo.launch {
        remoteDataSource.getUpcomingTvShows(currentPage, viewState)
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

