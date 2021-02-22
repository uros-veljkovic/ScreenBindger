package com.example.screenbindger.view.fragment.movie_details

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.screenbindger.db.remote.repo.ScreenBindgerRemoteDataSource
import com.example.screenbindger.db.remote.request.MarkAsFavoriteRequestBody
import com.example.screenbindger.util.constants.INTENT_REQUEST_CODE_INSTAGRAM
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.image.GalleryManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailsFragmentViewModel
@Inject constructor(
    val remoteDataSource: ScreenBindgerRemoteDataSource,
    var viewState: MutableLiveData<MovieDetailsFragmentViewState>,
    var viewAction: MutableLiveData<Event<MovieDetailsFragmentViewAction>>,
    var viewEvent: MutableLiveData<Event<MovieDetailsFragmentViewEvent>>,
    val galleryManager: GalleryManager
) : ViewModel() {

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
                remoteDataSource.getIsMovieFavorite(movieId, viewEvent)
            }
        }
    }

    fun setAction(action: MovieDetailsFragmentViewAction) {
        viewAction.postValue(Event(action))
    }

    fun reset() {
        viewState = MutableLiveData()
        viewAction = MutableLiveData()
    }

    fun manageFavorite() {
        val event = viewEvent.value?.peekContent()
        if (event != null &&
            (event is MovieDetailsFragmentViewEvent.IsLoadedAsFavorite ||
                    event is MovieDetailsFragmentViewEvent.AddedToFavorites)

        )
            markAsFavorite(false, movieId!!)
        else
            markAsFavorite(true, movieId!!)
    }

    fun markAsFavorite(isFavorite: Boolean, movieId: Int) {
        CoroutineScope(IO).launch {
            MarkAsFavoriteRequestBody(mediaId = movieId, favorite = isFavorite).let { body ->
                remoteDataSource.markAsFavorite(body, viewEvent)
            }
        }
    }

    fun fetchTrailers(movieId: Int) {
        CoroutineScope(IO).launch {
            viewEvent.postValue(Event(MovieDetailsFragmentViewEvent.Loading))
            remoteDataSource.getMovieTrailersInfo(movieId, viewEvent)
        }
    }

    fun saveToGalleryForInstagram(bitmap: Bitmap, context: Context, folderName: String) {
        viewEvent.postValue(Event(MovieDetailsFragmentViewEvent.Loading))
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
                viewEvent.postValue(Event(MovieDetailsFragmentViewEvent.PosterSaved(socialMediaCode)))
            } else {
                viewEvent.postValue(Event(MovieDetailsFragmentViewEvent.PosterNotSaved))
            }
        }
    }
}