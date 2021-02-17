package com.example.screenbindger.db.remote.service.review

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.view.fragment.review.ReviewFragmentViewEvent
import javax.inject.Inject

class ReviewService
@Inject constructor(
    val api: ReviewApi
) {

    suspend fun getMovieReviews(
        movieId: Int,
        viewEvent: MutableLiveData<Event<ReviewFragmentViewEvent>>
    ) {
        api.getMovieReviews(movieId = movieId).let { response ->
            if (response.isSuccessful) {
                val list = response.body()?.results ?: emptyList()
                if (list.isNotEmpty()) {
                    viewEvent.postValue(Event(ReviewFragmentViewEvent.ReviewsFetched(list)))
                } else {
                    viewEvent.postValue(Event(ReviewFragmentViewEvent.NoReviewsFetched))
                }
            } else {
                viewEvent.postValue(Event(ReviewFragmentViewEvent.Error("Error loading reviews for this movie...")))
            }
        }
    }
}