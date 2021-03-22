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
        viewModelScope.launch(IO) {
            launch {
                val newMovieViewState = remoteDataSource.getMovieDetails(movieId)
                this@MovieDetailsViewModel.showViewState = newMovieViewState
                movieViewStateProcessed.postValue(true)
            }
            launch {
                val newCastsViewState = remoteDataSource.getMovieCasts(movieId)
                this@MovieDetailsViewModel.castsViewState = newCastsViewState
                castsViewStateProcessed.postValue(true)
            }
            launch {
                val newEvent = remoteDataSource.getPeekIsFavoriteMovie(movieId)
                viewEvent.postValue(Event(newEvent))

                isFavorite = when (newEvent) {
                    is MarkedAsFavorite -> true
                    else -> false
                }
            }
        }
    }

    fun setAction(action: DetailsViewAction) {
        when (action) {
            is FetchTrailers -> fetchTrailers()
            is AddOrRemoveFromFavorites -> addOrRemoveFromFavorites()
        }
    }

    fun setEvent(event: DetailsViewEvent) {
        viewEvent.postValue(Event(event))
    }

    private fun addOrRemoveFromFavorites() {
        viewModelScope.launch(IO) {
            isFavorite = isFavorite.reverse()
            val requestBody = MarkAsFavoriteRequestBody(mediaId = movieId!!, favorite = isFavorite)
            val newEvent = remoteDataSource.postMarkAsFavorite(requestBody)

            when (newEvent) {
                is MarkedAsFavorite -> {
                    isFavorite = true
                }
                is MarkedAsNotFavorite -> {
                    isFavorite = false
                }
                else -> {
                }
            }
        }
    }

    fun fetchTrailers() {
        viewModelScope.launch(IO) {
            setEvent(Loading)
            val newEvent = remoteDataSource.getMovieTrailersInfo(movieId!!)
            setEvent(newEvent)
        }
    }

    fun saveToGalleryForInstagram(bitmap: Bitmap, context: Context, folderName: String) {
        viewEvent.postValue(Event(Loading))
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
                viewEvent.postValue(Event(PosterSaved(socialMediaCode)))
            } else {
                viewEvent.postValue(Event(PosterNotSaved(R.string.error_sharing_poster)))
            }
        }
    }
}