package com.example.screenbindger.view.fragment.favorite_movies

import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import javax.inject.Inject

class FavoriteMoviesViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val viewState: FavoriteMoviesViewState,
    val viewAction: FavoriteMoviesViewAction,
    val viewEvent: FavoriteMoviesViewEvent
) : ViewModel() {
}