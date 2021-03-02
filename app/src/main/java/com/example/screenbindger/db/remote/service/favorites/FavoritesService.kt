package com.example.screenbindger.db.remote.service.favorites

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.db.remote.request.MarkAsFavoriteRequestBody
import com.example.screenbindger.db.remote.session.Session
import com.example.screenbindger.model.domain.movie.generateGenres
import com.example.screenbindger.util.event.Event
import com.example.screenbindger.util.extensions.getErrorResponse
import com.example.screenbindger.util.extensions.ifLet
import com.example.screenbindger.view.fragment.details.DetailsFragmentViewEvent
import com.example.screenbindger.view.fragment.favorite_movies.FavoriteMoviesFragmentViewEvent
import javax.inject.Inject

class FavoritesService
@Inject constructor(
    val api: FavoritesApi
) {

    suspend fun getFavoriteMovieList(
        session: Session,
        viewEvent: MutableLiveData<Event<FavoriteMoviesFragmentViewEvent>>
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
                    viewEvent.postValue(Event(FavoriteMoviesFragmentViewEvent.EmptyList(message)))
                } else {
                    list.generateGenres()
                    viewEvent.postValue(Event(FavoriteMoviesFragmentViewEvent.MoviesLoaded(list)))
                }
            } else {
                message = "Error loading favorite movies :("
                viewEvent.postValue(Event(FavoriteMoviesFragmentViewEvent.Error(message)))
            }
        }
    }

    suspend fun getFavoriteTvShowList(
        session: Session,
        viewEvent: MutableLiveData<Event<FavoriteMoviesFragmentViewEvent>>
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
                    viewEvent.postValue(Event(FavoriteMoviesFragmentViewEvent.EmptyList(message)))
                } else {
                    list.generateGenres()
                    viewEvent.postValue(Event(FavoriteMoviesFragmentViewEvent.MoviesLoaded(list)))
                }
            } else {
                message = "Error loading favorite movies :("
                viewEvent.postValue(Event(FavoriteMoviesFragmentViewEvent.Error(message)))
            }
        }
    }

    suspend fun postMarkAsFavorite(
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

    suspend fun getPeekIsFavoriteMovie(
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

    suspend fun getPeekIsFavoriteTvShow(
        showId: Int,
        session: Session,
        viewEvent: MutableLiveData<Event<DetailsFragmentViewEvent>>
    ) {
        ifLet(session.id, session.accountId) {
            api.getFavoriteTvShowList(
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