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

    fun fetchMovies() = coroutineIo.launch {
        remoteDataSource.getUpcomingMovies(viewState)
    }

    fun fetchTvShows() = coroutineIo.launch {
        remoteDataSource.getUpcomingTvShows(viewState)
    }

    fun peekLastAction(): UpcomingViewAction {
        return viewAction.value!!
    }

    fun setAction(action: UpcomingViewAction) {
        viewAction.postValue(action)
    }


}