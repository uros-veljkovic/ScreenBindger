package com.example.screenbindger.db.remote.service.favorites

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.db.remote.request.MarkAsFavoriteRequestBody
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
import com.example.screenbindger.util.extensions.ifLet
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewEvent
import javax.inject.Inject

class FavoritesService
@Inject constructor(
    val api: FavoritesApi
){

    suspend fun postSetShowAsFavorite(
        session: Session,
        body: MarkAsFavoriteRequestBody,
        viewEffect: MutableLiveData<Event<DetailsFragmentViewEvent>>
    ) {
        ifLet(session.id, session.accountId) {
            api.postMarkAsFavorite(
                sessionId = session.id!!,
                accountId = session.accountId!!,
                body = body
            ).let {
                if (it.isSuccessful) {
                    if (body.favorite)
                        viewEffect.postValue(Event(DetailsFragmentViewEvent.AddedToFavorites()))
                    else
                        viewEffect.postValue(Event(DetailsFragmentViewEvent.RemovedFromFavorites()))
                } else {
                    val error = it.getErrorResponse().statusMessage
                    viewEffect.postValue(Event(DetailsFragmentViewEvent.Error(error)))
                }
            }
        }
    }

    suspend fun getPeekIsFavorite(
        showId: Int,
        session: Session,
        viewEvent: MutableLiveData<Event<DetailsFragmentViewEvent>>
    ) {
        ifLet(session.id, session.accountId) {
            api.getFavoriteMovieList(
                sessionId = session.id!!,
                accountId = session.accountId!!
            ).let { response ->
                if (response.isSuccessful) {
                    response.body()?.list?.forEach { show ->
                        if (show.id!! == showId) {
                            viewEvent.postValue(Event(DetailsFragmentViewEvent.IsLoadedAsFavorite))
                            return
                        }
                    }
                    viewEvent.postValue(Event(DetailsFragmentViewEvent.IsLoadedAsNotFavorite))
                } else {
                    viewEvent.postValue(
                        Event(
                            DetailsFragmentViewEvent.Error(
                                "Error finding out if this is you favorite movie :("
                            )
                        )
                    )
                }
            }
        }
    }
}