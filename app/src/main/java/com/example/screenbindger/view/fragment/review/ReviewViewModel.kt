package com.example.screenbindger.view.fragment.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.util.event.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReviewViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val viewState: MutableLiveData<ReviewViewState>,
    val viewAction: MutableLiveData<Event<ReviewViewAction>>,
    val viewEvent: MutableLiveData<Event<ReviewViewEvent>>
) : ViewModel(){

    fun fetchReviews(movieId: Int) {
        viewEvent.postValue(Event(ReviewViewEvent.Loading))
        CoroutineScope(IO).launch {
            remoteDataSource.getMovieReviews(movieId, viewEvent)
        }
    }

}