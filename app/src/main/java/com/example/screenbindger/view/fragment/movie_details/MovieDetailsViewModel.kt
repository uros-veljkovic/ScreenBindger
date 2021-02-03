package com.example.screenbindger.view.fragment.movie_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.response.MovieDetailsCastResponse
import com.example.screenbindger.model.domain.MovieEntity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import javax.inject.Inject

class MovieDetailsViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource
) : ViewModel() {

    var responseMovieDetails: MutableLiveData<Response<MovieEntity>?> =
        MutableLiveData(null)
    var responseMovieDetailsCast: MutableLiveData<Response<MovieDetailsCastResponse>?> =
        MutableLiveData(null)

    fun fetchData(movieId: Int) {
        runBlocking {
            launch(IO) {
                val response = remoteDataSource.getMovieDetails(movieId)
                responseMovieDetails.postValue(response)
            }
            launch(IO) {
                val response = remoteDataSource.getMovieCasts(movieId)
                responseMovieDetailsCast.postValue(response)
            }
        }

    }

    fun reset(){
        responseMovieDetailsCast.value = null
        responseMovieDetails.value = null
    }

}