package com.example.screenbindger.view.fragment.genres

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.response.genre.AllGenresResponse
import com.example.screenbindger.model.domain.genre.GenreEntity
import com.example.screenbindger.view.fragment.ShowListViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class GenresViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    val viewState: MutableLiveData<GenresViewState>
) : ViewModel() {

    init {
        executeActionAndSetState { remoteDataSource.getGenres() }
    }

    private fun executeActionAndSetState(actionReturningState: suspend () -> GenresViewState) =
        viewModelScope.launch(IO) {
            val newState = actionReturningState()
            viewState.postValue(newState)
        }
}