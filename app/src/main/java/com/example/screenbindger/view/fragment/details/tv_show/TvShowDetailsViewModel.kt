package com.example.screenbindger.view.fragment.details.tv_show

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
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewAction
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewEvent
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TvShowDetailsViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    var viewState: DetailsFragmentViewState,
    var viewAction: MutableLiveData<Event<DetailsFragmentViewAction>>,
    var viewEvent: MutableLiveData<Event<DetailsFragmentViewEvent>>,
    val galleryManager: GalleryManager
) : ViewModel() {

    var trailer: TrailerDetails? = null
    private var showId: Int? = null

    fun fetchData(showId: Int) {
        this.showId = showId
        CoroutineScope(Dispatchers.IO).launch {
            launch {
                remoteDataSource.getTvShowDetails(showId, viewState)
            }
            launch {
                remoteDataSource.getTvShowCasts(showId, viewState)
            }
            launch {
                remoteDataSource.getPeekIsFavoriteTvShow(showId, viewEvent)
            }
        }
    }

    fun setAction(action: DetailsFragmentViewAction) {
        viewAction.postValue(Event(action))
    }

    fun setEvent(event: DetailsFragmentViewEvent) {
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
            (event is DetailsFragmentViewEvent.IsLoadedAsFavorite ||
                    event is DetailsFragmentViewEvent.AddedToFavorites)

        )
            markAsFavorite(false, showId!!)
        else
            markAsFavorite(true, showId!!)
    }

    fun markAsFavorite(isFavorite: Boolean, movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            MarkAsFavoriteRequestBody(
                mediaType = "tv",
                mediaId = movieId,
                favorite = isFavorite
            ).let { body ->
                remoteDataSource.postMarkAsFavorite(body, viewEvent)
            }
        }
    }

    fun fetchTrailers(showId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            viewEvent.postValue(Event(DetailsFragmentViewEvent.Loading))
            remoteDataSource.getTvShowTrailers(showId, viewEvent)
        }
    }

    fun saveToGalleryForInstagram(bitmap: Bitmap, context: Context, folderName: String) {
        viewEvent.postValue(Event(DetailsFragmentViewEvent.Loading))
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
                viewEvent.postValue(Event(DetailsFragmentViewEvent.PosterSaved(socialMediaCode)))
            } else {
                viewEvent.postValue(Event(DetailsFragmentViewEvent.PosterNotSaved))
            }
        }
    }
}