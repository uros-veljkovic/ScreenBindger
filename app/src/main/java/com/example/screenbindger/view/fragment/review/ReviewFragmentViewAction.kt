package com.example.screenbindger.view.fragment.review

import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource_Factory
import javax.inject.Inject

sealed class ReviewFragmentViewAction {
    object FetchReviews : ReviewFragmentViewAction()
}