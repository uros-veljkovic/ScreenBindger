package com.example.screenbindger.db.remote.service.favorites

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.R
import com.example.screenbindger.db.remote.request.MarkAsFavoriteRequestBody
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.domain.movie.generateGenres
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
import com.example.screenbindger.util.extensions.ifLet
import com.example.screenbindger.view.fragment.details.NetworkError
import com.example.screenbindger.view.fragment.details.DetailsViewEvent
import com.example.screenbindger.view.fragment.details.MarkedAsFavorite
import com.example.screenbindger.view.fragment.details.MarkedAsNotFavorite
import com.example.screenbindger.view.fragment.favorite_movies.FavoritesViewEvent
import javax.inject.Inject

class FavoritesService
@Inject constructor(
    val api: FavoritesApi
) {

    suspend fun getFavoriteMovieList(
        session: Session,
        viewEvent: MutableLiveData<Event<FavoritesViewEvent>>
    ) {
        api.getFavoriteMovieList(
            sessionId = session.id!!,
            accountId = session.accountId!!
        ).let { response ->
            var message = ""
            if (response.isSuccessful) {
                val list = response.body()?.list
                if (list.isNullOrEmpty()) {
                    message = "No favorite movies added so far."
                    viewEvent.postValue(Event(FavoritesViewEvent.EmptyList(message)))
                } else {
                    list.generateGenres()
                    viewEvent.postValue(Event(FavoritesViewEvent.ListLoaded(list)))
                }
            } else {
                message = "Error loading favorite movies :("
                viewEvent.postValue(Event(FavoritesViewEvent.Error(message)))
            }
        }
    }

    suspend fun getFavoriteTvShowList(
        session: Session,
        viewEvent: MutableLiveData<Event<FavoritesViewEvent>>
    ) {
        api.getFavoriteTvShowList(
            sessionId = session.id!!,
            accountId = session.accountId!!
        ).let { response ->
            var message = ""
            if (response.isSuccessful) {
                val list = response.body()?.list
                if (list.isNullOrEmpty()) {
                    message = "No favorite movies added so far."
                    viewEvent.postValue(Event(FavoritesViewEvent.EmptyList(message)))
                } else {
                    list.generateGenres()
                    viewEvent.postValue(Event(FavoritesViewEvent.ListLoaded(list)))
                }
            } else {
                message = "Error loading favorite movies :("
                viewEvent.postValue(Event(FavoritesViewEvent.Error(message)))
            }
        }
    }

    suspend fun postMarkAsFavorite(
        session: Session,
        body: MarkAsFavoriteRequestBody
    ): DetailsViewEvent {
        return try {
            api.postMarkAsFavorite(
                sessionId = session.id!!,
                accountId = session.accountId!!,
                body = body
            ).let {
                if (it.isSuccessful) {
                    if (body.favorite)
                        MarkedAsFavorite
                    else
                        MarkedAsNotFavorite
                } else {
                    NetworkError(R.string.error_post_as_favorite)
                }
            }
        } catch (e: Exception) {
            NetworkError(R.string.network_error)
        }
    }

    suspend fun getPeekIsFavoriteMovie(
        showId: Int,
        session: Session
    ): DetailsViewEvent {
        return try {
            api.getFavoriteMovieList(
                sessionId = session.id!!,
                accountId = session.accountId!!
            ).let { response ->
                if (response.isSuccessful) {
                    response.body()?.list?.forEach { show ->
                        if (show.id!! == showId) {
                            MarkedAsFavorite
                        }
                    }
                    MarkedAsNotFavorite
                } else {
                    NetworkError(R.string.error_peek_is_favorite)
                }
            }
        } catch (e: Exception) {
            NetworkError(R.string.network_error)
        }
    }

    suspend fun getPeekIsFavoriteTvShow(
        showId: Int,
        session: Session,
        viewEvent: MutableLiveData<Event<DetailsViewEvent>>
    ) {
        ifLet(session.id, session.accountId) {
            api.getFavoriteTvShowList(
                sessionId = session.id!!,
                accountId = session.accountId!!
            ).let { response ->
                if (response.isSuccessful) {
                    response.body()?.list?.forEach { show ->
                        if (show.id!! == showId) {
                            viewEvent.postValue(Event(MarkedAsFavorite))
                            return
                        }
                    }
                    viewEvent.postValue(Event(MarkedAsNotFavorite))
                } else {
                    viewEvent.postValue(
                        Event(NetworkError(R.string.network_error))
                    )
                }
            }
        }
    }
}