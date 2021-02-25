package com.example.screenbindger.view.fragment.upcoming

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpcomingFragmentViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val upcomingViewState: MutableLiveData<UpcomingFragmentViewState>,
    val viewAction: MutableLiveData<UpcomingFragmentViewAction>
) : ViewModel() {

    fun fetchMovies() {
        CoroutineScope(IO).launch {
            remoteDataSource.getUpcomingMovies(upcomingViewState)
        }
    }

    fun fetchTvShows() {
        CoroutineScope(IO).launch {
            remoteDataSource.getUpcomingTvShows(upcomingViewState)
        }
    }

    fun setAction(action: UpcomingFragmentViewAction) {
        viewAction.postValue(action)
    }



}