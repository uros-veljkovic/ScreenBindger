package com.example.screenbindger.view.fragment.movie_details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDatabase
import com.example.screenbindger.db.remote.response.MovieDetailsCastResponse
import com.example.screenbindger.model.domain.MovieEntity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import javax.inject.Inject

class MovieDetailsViewModel
@Inject constructor(
    val db: ScreenBindgerRemoteDatabase
) : ViewModel() {

    var responseMovieDetails: MutableLiveData<Response<MovieEntity>?> =
        MutableLiveData(null)
    var responseMovieDetailsCast: MutableLiveData<Response<MovieDetailsCastResponse>?> =
        MutableLiveData(null)

    fun fetchData(movieId: Int) {
        runBlocking {
            launch(IO) {
                val response = db.getMovieDetails(movieId)
                responseMovieDetails.postValue(response)
            }
            launch(IO) {
                val response = db.getMovieCasts(movieId)
                responseMovieDetailsCast.postValue(response)
            }
        }

    }

    fun reset(){
        responseMovieDetailsCast.value = null
        responseMovieDetails.value = null
    }

}