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

class MovieDetailsViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    var viewState: MutableLiveData<MovieDetailsViewState>,
    var viewAction: MutableLiveData<Event<MovieDetailsViewAction>>,
    var viewEvent: MutableLiveData<Event<MovieDetailsViewEvent>>
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

    fun setAction(action: MovieDetailsViewAction) {
        viewAction.postValue(Event(action))
    }

    fun reset() {
        viewState = MutableLiveData()
        viewAction = MutableLiveData()
    }

    fun markAsFavorite(movieId: Int) {
        CoroutineScope(IO).launch {
            MarkAsFavoriteRequestBody(mediaId = movieId, favorite = true).let { body ->
                remoteDataSource.markAsFavorite(body, viewEvent)
            }
        }
    }

}