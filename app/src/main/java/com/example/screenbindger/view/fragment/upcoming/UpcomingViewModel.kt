package com.example.screenbindger.view.fragment.upcoming

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
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

    fun nextPageMovies() {
        currentPage++
        fetchMovies()
    }

    fun previousPageMovies() {
        currentPage--
        fetchMovies()
    }

    fun nextPageTvShows() {
        currentPage++
        fetchTvShows()
    }

    fun previousPageTvShows() {
        currentPage--
        fetchTvShows()
    }

    fun setAction(action: UpcomingViewAction) {
        viewAction.postValue(action)
    }

    fun getState(): UpcomingViewState = viewState.value!!

}

