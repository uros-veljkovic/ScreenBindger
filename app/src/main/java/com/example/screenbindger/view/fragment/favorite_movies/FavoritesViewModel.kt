package com.example.screenbindger.view.fragment.favorite_movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        viewAction.postValue(Event(FavoritesViewAction.FetchMovies))
    }

    fun fetchFavoriteMovies() {
        viewModelScope.launch {
            remoteDataSource.getFavoriteMovieList(viewEvent)
        }
    }

    fun fetchFavoriteTvShows() {
        viewModelScope.launch {
            remoteDataSource.getFavoriteTvShowList(viewEvent)
        }
    }

    fun setState(state: FavoritesViewState) {
        viewState.postValue(state)
    }

    fun setAction(action: FavoritesViewAction) {
        viewAction.postValue(Event(action))
    }

    fun peekLastAction(): FavoritesViewAction {
        return viewAction.value!!.peekContent()
    }
}