package com.example.screenbindger.view.fragment.review

import com.example.screenbindger.model.domain.review.ReviewEntity

sealed class ReviewFragmentViewEvent {
    data class ReviewsFetched(val list: List<ReviewEntity>) : ReviewFragmentViewEvent()
    object NoReviewsFetched : ReviewFragmentViewEvent()
    data class Error(val message: String) : ReviewFragmentViewEvent()
}