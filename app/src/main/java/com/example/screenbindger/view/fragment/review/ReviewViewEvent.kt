package com.example.screenbindger.view.fragment.review

import com.example.screenbindger.model.domain.review.ReviewEntity

sealed class ReviewViewEvent {
    data class ReviewsFetched(val list: List<ReviewEntity>) : ReviewViewEvent()
    object NoReviewsFetched : ReviewViewEvent()
    object Loading : ReviewViewEvent()

    data class Error(val message: String) : ReviewViewEvent()
}