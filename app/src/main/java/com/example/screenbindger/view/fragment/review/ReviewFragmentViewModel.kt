package com.example.screenbindger.view.fragment.review

import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import javax.inject.Inject

class ReviewFragmentViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val viewState: ReviewFragmentViewState,
    val viewAction: ReviewFragmentViewAction,
    val viewEvent: ReviewFragmentViewEvent
)