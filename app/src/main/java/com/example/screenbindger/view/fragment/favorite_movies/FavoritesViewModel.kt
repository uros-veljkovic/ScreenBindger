package com.example.screenbindger.view.fragment.favorite_movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.util.event.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val viewState: MutableLiveData<FavoritesViewState>,
    val viewAction: MutableLiveData<Event<FavoritesViewAction>>,
    val viewEvent: MutableLiveData<Event<FavoritesViewEvent>>
) : ViewModel() {

    init {
//        loadFavorites()
        viewAction.postValue(Event(FavoritesViewAction.FetchMovies))
    }

    fun fetchFavorites() {
        CoroutineScope(IO).launch {
            remoteDataSource.getFavoriteMovieList(viewEvent)
        }
    }

    fun setState(state: FavoritesViewState) {
        viewState.postValue(state)
    }
}