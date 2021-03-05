package com.example.screenbindger.db.remote.service.review

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.view.fragment.review.ReviewViewEvent
import javax.inject.Inject

class ReviewService
@Inject constructor(
    val api: ReviewApi
) {

    suspend fun getMovieReviews(
        movieId: Int,
        viewEvent: MutableLiveData<Event<ReviewViewEvent>>
    ) {
        api.getMovieReviews(movieId = movieId).let { response ->
            if (response.isSuccessful) {
                val list = response.body()?.results ?: emptyList()
                if (list.isNotEmpty()) {
                    viewEvent.postValue(Event(ReviewViewEvent.ReviewsFetched(list)))
                } else {
                    viewEvent.postValue(Event(ReviewViewEvent.NoReviewsFetched))
                }
            } else {
                viewEvent.postValue(Event(ReviewViewEvent.Error("Error loading reviews for this movie...")))
            }
        }
    }
}