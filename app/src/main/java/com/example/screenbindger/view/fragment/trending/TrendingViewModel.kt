package com.example.screenbindger.view.fragment.trending

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDatabase
import com.example.screenbindger.db.remote.response.TrendingMoviesResponse
import com.example.screenbindger.model.domain.MovieEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Response

class TrendingViewModel
@ViewModelInject constructor(
    val db: ScreenBindgerRemoteDatabase
) : ViewModel() {

    var response: MutableLiveData<Response<TrendingMoviesResponse>?> = MutableLiveData(null)

    init {
        fetchTrendingMovies()
    }

    fun fetchTrendingMovies() {
        CoroutineScope(IO).launch {
            response.postValue(db.getTrending())
        }
    }

}