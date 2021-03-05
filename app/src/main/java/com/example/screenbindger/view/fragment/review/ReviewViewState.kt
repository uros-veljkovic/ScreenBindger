package com.example.screenbindger.view.fragment.review

import com.example.screenbindger.model.domain.review.ReviewEntity

sealed class ReviewViewState {
    data class ReviewsFetched(val list: List<ReviewEntity>) : ReviewViewState()
    object ReviewsNotFetched : ReviewViewState()
}