package com.example.screenbindger.view.fragment.upcoming

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.view.fragment.trending.TrendingFragmentViewAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpcomingFragmentViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val upcomingViewState: MutableLiveData<UpcomingFragmentViewState>,
    val viewAction: MutableLiveData<UpcomingFragmentViewAction>,
    private val coroutineIo: CoroutineScope
) : ViewModel() {

    fun fetchMovies() = coroutineIo.launch {
        remoteDataSource.getUpcomingMovies(upcomingViewState)
    }

    fun fetchTvShows() = coroutineIo.launch {
        remoteDataSource.getUpcomingTvShows(upcomingViewState)
    }

    fun peekLastAction(): UpcomingFragmentViewAction {
        return viewAction.value!!
    }

    fun setAction(action: UpcomingFragmentViewAction) {
        viewAction.postValue(action)
    }


}