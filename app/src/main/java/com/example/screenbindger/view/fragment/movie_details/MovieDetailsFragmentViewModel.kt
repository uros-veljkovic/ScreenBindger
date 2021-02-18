package com.example.screenbindger.view.fragment.movie_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.request.MarkAsFavoriteRequestBody
import com.example.screenbindger.util.event.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailsFragmentViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    var viewState: MutableLiveData<MovieDetailsFragmentViewState>,
    var viewAction: MutableLiveData<Event<MovieDetailsFragmentViewAction>>,
    var viewEvent: MutableLiveData<Event<MovieDetailsFragmentViewEvent>>
) : ViewModel() {

    fun fetchData(movieId: Int) {
        CoroutineScope(IO).launch {
            launch {
                remoteDataSource.getMovieDetails(movieId, viewState)
            }
            launch {
                remoteDataSource.getMovieCasts(movieId, viewState)
            }
            launch {
                checkIsFavorite(movieId)
            }
        }

    }

    fun setAction(action: MovieDetailsFragmentViewAction) {
        viewAction.postValue(Event(action))
    }

    fun reset() {
        viewState = MutableLiveData()
        viewAction = MutableLiveData()
    }

    fun markAsFavorite(isFavorite: Boolean, movieId: Int) {
        CoroutineScope(IO).launch {
            MarkAsFavoriteRequestBody(mediaId = movieId, favorite = isFavorite).let { body ->
                remoteDataSource.markAsFavorite(body, viewEvent)
            }
        }
    }

    private fun checkIsFavorite(movieId: Int) {
        CoroutineScope(IO).launch {
            remoteDataSource.getIsMovieFavorite(movieId, viewEvent)
        }
    }

}