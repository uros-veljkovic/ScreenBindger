package com.example.screenbindger.view.fragment.movie_details

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.cast.CastEntity
import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.util.event.Event

data class MovieDetailsFragmentViewState(
    val eventState: MutableLiveData<Event<MovieDetailsState>?> = MutableLiveData(null),
    var movie: ShowEntity? = null,
    var casts: List<CastEntity>? = null
) {

    fun currentState(): MovieDetailsState? = eventState.value?.peekContent()

    fun prepareForFinalState() {
        if (casts.isNullOrEmpty() && movie == null) {
            eventState.postValue(Event(MovieDetailsState.NoDataAvailable))
        } else {
            eventState.postValue(Event(MovieDetailsState.FetchedAll))
        }
    }

    fun setState(state: MovieDetailsState){
        eventState.postValue(Event(state))
    }
}

sealed class MovieDetailsState {
    object Fetching : MovieDetailsState()
    object MovieProcessed : MovieDetailsState()
    object CastsProcessed : MovieDetailsState()
    object FetchedAll : MovieDetailsState()
    object NoDataAvailable : MovieDetailsState()

    sealed class Error(val message: String) : MovieDetailsState() {
        data class MovieNotFetched(val m: String) : Error("")
        data class CastsNotFetched(val m: String) : Error("")
    }
}