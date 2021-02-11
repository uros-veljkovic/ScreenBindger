package com.example.screenbindger.view.fragment.favorite_movies

import android.util.EventLog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.util.event.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteMoviesViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val viewState: MutableLiveData<FavoriteMoviesViewState>,
    val viewAction: MutableLiveData<Event<FavoriteMoviesViewAction>>,
    val viewEvent: MutableLiveData<Event<FavoriteMoviesViewEvent>>
) : ViewModel() {

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        CoroutineScope(IO).launch {
            remoteDataSource.getFavoriteMovieList(viewEvent)
        }
    }
}