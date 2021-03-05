package com.example.screenbindger.view.fragment.details

import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.cast.CastEntity
import com.example.screenbindger.model.domain.movie.ShowEntity
import com.example.screenbindger.util.event.Event

data class DetailsFragmentViewState(
    val eventState: MutableLiveData<Event<ShowDetailsState>?> = MutableLiveData(null),
    var show: ShowEntity? = null,
    var casts: List<CastEntity>? = null
) {

    fun currentState(): ShowDetailsState? = eventState.value?.peekContent()

    fun prepareForFinalState() {
        if (casts.isNullOrEmpty() && show == null) {
            eventState.postValue(Event(ShowDetailsState.NoDataAvailable))
        } else {
            eventState.postValue(Event(ShowDetailsState.FetchedAll))
        }
    }

    fun setState(state: ShowDetailsState){
        eventState.postValue(Event(state))
    }
}

sealed class ShowDetailsState {
    object Fetching : ShowDetailsState()
    object ShowProcessed : ShowDetailsState()
    object CastsProcessed : ShowDetailsState()
    object FetchedAll : ShowDetailsState()
    object NoDataAvailable : ShowDetailsState()

    sealed class Error(val message: String) : ShowDetailsState() {
        data class ShowNotFetched(val m: String) : Error("")
        data class CastsNotFetched(val m: String) : Error("")
    }
}