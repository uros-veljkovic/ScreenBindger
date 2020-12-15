package com.example.screenbindger.view.fragment.movie_details

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDatabase
import com.example.screenbindger.db.remote.response.MovieDetailsCastResponse
import com.example.screenbindger.model.domain.MovieEntity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class MovieDetailsViewModel
@ViewModelInject constructor(
    val db: ScreenBindgerRemoteDatabase
) : ViewModel() {

    var responseMovieDetails: MutableLiveData<Response<MovieEntity>?> =
        MutableLiveData(null)
    var responseMovieDetailsCast: MutableLiveData<Response<MovieDetailsCastResponse>?> =
        MutableLiveData(null)


    fun fetchData(movieId: Int) {
        // 577922
        runBlocking {
            launch(IO) {
                val response = db.getMovieDetails(movieId)
                Log.d("MovieDetails", "fetchData: ${response.body()}")
                responseMovieDetails.postValue(response)
            }
            launch(IO) {
                val response = db.getMovieCasts(movieId)
                Log.d("MovieDetails", "fetchData: ${response.body()}")
                responseMovieDetailsCast.postValue(response)
            }
        }

    }

}