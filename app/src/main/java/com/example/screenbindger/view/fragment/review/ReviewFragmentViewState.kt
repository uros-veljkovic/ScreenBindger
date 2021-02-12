package com.example.screenbindger.view.fragment.review

import com.example.screenbindger.db.remote.response.review.ReviewsResponse

sealed class ReviewFragmentViewState {
    data class ReviewsFetched(val list: ReviewsResponse) : ReviewFragmentViewState()
    object ReviewsNotFetched : ReviewFragmentViewState()
}