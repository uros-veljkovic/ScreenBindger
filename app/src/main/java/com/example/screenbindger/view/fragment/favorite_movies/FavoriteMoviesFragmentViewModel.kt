package com.example.screenbindger.view.fragment.favorite_movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.util.event.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteMoviesFragmentViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val viewState: MutableLiveData<FavoriteMoviesFragmentViewState>,
    val viewAction: MutableLiveData<Event<FavoriteMoviesFragmentViewAction>>,
    val viewEvent: MutableLiveData<Event<FavoriteMoviesFragmentViewEvent>>
) : ViewModel() {

    init {
//        loadFavorites()
        viewAction.postValue(Event(FavoriteMoviesFragmentViewAction.FetchMovies))
    }

    fun fetchFavorites() {
        CoroutineScope(IO).launch {
            remoteDataSource.getFavoriteMovieList(viewEvent)
        }
    }
}