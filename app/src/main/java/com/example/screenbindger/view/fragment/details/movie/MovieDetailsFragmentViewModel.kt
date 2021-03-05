package com.example.screenbindger.view.fragment.details.movie

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.request.MarkAsFavoriteRequestBody
import com.example.screenbindger.db.remote.response.movie.trailer.TrailerDetails
import com.example.screenbindger.util.constants.INTENT_REQUEST_CODE_INSTAGRAM
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.image.GalleryManager
import com.example.screenbindger.view.fragment.details.DetailsViewAction
import com.example.screenbindger.view.fragment.details.DetailsViewEvent
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailsFragmentViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    var viewState: DetailsFragmentViewState,
    var viewAction: MutableLiveData<Event<DetailsViewAction>>,
    var viewEvent: MutableLiveData<Event<DetailsViewEvent>>,
    val galleryManager: GalleryManager
) : ViewModel() {

    var trailer: TrailerDetails? = null
    private var movieId: Int? = null

    fun fetchData(movieId: Int) {
        this.movieId = movieId
        CoroutineScope(IO).launch {
            launch {
                remoteDataSource.getMovieDetails(movieId, viewState)
            }
            launch {
                remoteDataSource.getMovieCasts(movieId, viewState)
            }
            launch {
                remoteDataSource.getPeekIsFavoriteMovie(movieId, viewEvent)
            }
        }
    }

    fun setAction(action: DetailsViewAction) {
        viewAction.postValue(Event(action))
    }

    fun setEvent(event: DetailsViewEvent) {
        viewEvent.postValue(Event(event))
    }

    fun reset() {
        viewState =
            DetailsFragmentViewState()
        viewAction = MutableLiveData()
    }

    fun manageFavorite() {
        val event = viewEvent.value?.peekContent()
        if (event != null &&
            (event is DetailsViewEvent.IsLoadedAsFavorite ||
                    event is DetailsViewEvent.AddedToFavorites)

        )
            markAsFavorite(false, movieId!!)
        else
            markAsFavorite(true, movieId!!)
    }

    fun markAsFavorite(isFavorite: Boolean, movieId: Int) {
        CoroutineScope(IO).launch {
            MarkAsFavoriteRequestBody(mediaId = movieId, favorite = isFavorite).let { body ->
                remoteDataSource.postMarkAsFavorite(body, viewEvent)
            }
        }
    }

    fun fetchTrailers(movieId: Int) {
        CoroutineScope(IO).launch {
            viewEvent.postValue(Event(DetailsViewEvent.Loading))
            remoteDataSource.getMovieTrailersInfo(movieId, viewEvent)
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
                viewEvent.postValue(Event(DetailsViewEvent.PosterNotSaved))
            }
        }
    }
}