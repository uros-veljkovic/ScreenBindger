package com.example.screenbindger.view.fragment.trending

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrendingFragmentViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val viewState: MutableLiveData<TrendingFragmentViewState>,
    val viewAction: MutableLiveData<TrendingFragmentViewAction>,
    private val coroutineIo: CoroutineScope
) : ViewModel() {

    fun fetchMovies() = coroutineIo.launch {
        remoteDataSource.getTrendingMovies(viewState)
    }

    fun fetchTvShows() = coroutineIo.launch {
        remoteDataSource.getTrendingTvShows(viewState)
    }

    fun peekLastAction(): TrendingFragmentViewAction {
        return viewAction.value!!
    }

    fun setAction(action: TrendingFragmentViewAction) {
        viewAction.postValue(action)
    }

    fun tearDown() {
        viewState.postValue(TrendingFragmentViewState())
        viewAction.postValue(TrendingFragmentViewAction.FetchMovies)
    }

}