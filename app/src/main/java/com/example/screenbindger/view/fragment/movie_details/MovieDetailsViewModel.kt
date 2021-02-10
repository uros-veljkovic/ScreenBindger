package com.example.screenbindger.view.fragment.movie_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailsViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    var viewState: MutableLiveData<MovieDetailsViewState>,
    var viewAction: MutableLiveData<MovieDetailsViewAction>
) : ViewModel() {

    fun fetchData(movieId: Int) {
        CoroutineScope(IO).launch {
            launch {
                remoteDataSource.getMovieDetails(movieId, viewState)
            }
            launch {
                remoteDataSource.getMovieCasts(movieId, viewState)
            }
        }

    }

    fun reset() {
        viewState = MutableLiveData()
        viewAction = MutableLiveData()
    }

}