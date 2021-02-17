package com.example.screenbindger.view.fragment.review

import com.example.screenbindger.db.remote.response.review.MovieReviewsResponse
import com.example.screenbindger.model.domain.review.ReviewEntity

sealed class ReviewFragmentViewState {
    data class ReviewsFetched(val list: List<ReviewEntity>) : ReviewFragmentViewState()
    object ReviewsNotFetched : ReviewFragmentViewState()
}