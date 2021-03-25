package com.example.screenbindger.view.fragment.details.movie

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.screenbindger.R
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.request.MarkAsFavoriteRequestBody
import com.example.screenbindger.db.remote.response.movie.trailer.TrailerDetails
import com.example.screenbindger.util.constants.INTENT_REQUEST_CODE_INSTAGRAM
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.reverse
import com.example.screenbindger.util.image.GalleryManager
import com.example.screenbindger.view.fragment.details.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailsViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    var showViewState: ShowViewState,
    var castsViewState: CastsViewState,
    var viewEvent: MutableLiveData<Event<DetailsViewEvent>>,
    val galleryManager: GalleryManager
) : ViewModel() {

    var trailer: TrailerDetails? = null
    private var movieId: Int? = null
    private var isFavorite: Boolean = false

    private val movieViewStateProcessed: MutableLiveData<Boolean> = MutableLiveData(false)
    private val castsViewStateProcessed: MutableLiveData<Boolean> = MutableLiveData(false)
    val dataProcessed: MediatorLiveData<Boolean> = MediatorLiveData()

    init {
        dataProcessed.addSource(castsViewStateProcessed) {
            dataProcessed.postValue(movieViewStateProcessed.value!! && castsViewStateProcessed.value!!)
        }
        dataProcessed.addSource(movieViewStateProcessed) {
            dataProcessed.postValue(movieViewStateProcessed.value!! && castsViewStateProcessed.value!!)
        }
    }

    fun fetchData(movieId: Int) {
        this.movieId = movieId
        viewModelScope.launch {
            launch {
                fetchMovieDetails()
            }
            launch {
                fetchCastsDetails()
            }
            launch {
                fetchTrailers()
            }
            launch {
                peakIsFavorite()
            }
        }
    }

    private suspend fun peakIsFavorite() {
        val newEvent = remoteDataSource.getPeekIsFavoriteMovie(movieId!!)
        setEvent(newEvent)

        isFavorite = when (newEvent) {
            is DetailsViewEvent.MarkedAsFavorite -> true
            else -> false
        }
    }

    private suspend fun fetchCastsDetails() {
        val newCastsViewState = remoteDataSource.getMovieCasts(movieId!!)
        this.castsViewState = newCastsViewState
        castsViewStateProcessed.postValue(true)
    }

    private suspend fun fetchMovieDetails() {
        val newMovieViewState = remoteDataSource.getMovieDetails(movieId!!)
        this.showViewState = newMovieViewState
        movieViewStateProcessed.postValue(true)
    }

    fun setEvent(event: DetailsViewEvent) {
        viewEvent.postValue(Event(event))
    }

    fun addOrRemoveFromFavorites() {
        viewModelScope.launch {
            isFavorite = isFavorite.reverse()
            val requestBody = MarkAsFavoriteRequestBody(mediaId = movieId!!, favorite = isFavorite)
            val newEvent = remoteDataSource.postMarkAsFavorite(requestBody)

            when (newEvent) {
                is DetailsViewEvent.MarkedAsFavorite -> {
                    isFavorite = true
                }
                is DetailsViewEvent.MarkedAsNotFavorite -> {
                    isFavorite = false
                }
                else -> {
                }
            }
            setEvent(newEvent)
        }
    }

    fun fetchTrailers() {
        viewModelScope.launch {
            val newEvent = remoteDataSource.getMovieTrailersInfo(movieId!!)
            setEvent(newEvent)
        }
    }

    fun saveToGalleryForInstagram(bitmap: Bitmap, context: Context, folderName: String) {
        viewEvent.postValue(Event(DetailsViewEvent.Loading))
        saveToGallery(
            INTENT_REQUEST_CODE_INSTAGRAM,
            bitmap,
            context,
            folderName
        )
    }

    private fun saveToGallery(
        socialMediaCode: Int,
        bitmap: Bitmap,
        context: Context,
        folderName: String
    ) {
        galleryManager.saveImage(bitmap, context, folderName).let { isSaved ->
            if (isSaved) {
                viewEvent.postValue(Event(DetailsViewEvent.PosterSaved(socialMediaCode)))
            } else {
                viewEvent.postValue(Event(DetailsViewEvent.PosterNotSaved(R.string.error_sharing_poster)))
            }
        }
    }
}