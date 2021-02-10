package com.example.screenbindger.view.fragment.movie_details

import com.example.screenbindger.model.domain.CastEntity
import com.example.screenbindger.model.domain.MovieEntity
import com.example.screenbindger.util.event.Event

data class MovieDetailsViewState(
    val eventState: Event<MovieDetailsState?> = Event(null),
    val movie: MovieEntity? = null,
    val casts: List<CastEntity>? = null
)

sealed class MovieDetailsState {
    object Fetching : MovieDetailsState()
    object MovieFetched : MovieDetailsState()
    object CastsFetched : MovieDetailsState()

    sealed class Error(val message: String) : MovieDetailsState() {
        data class MovieNotFetched(val m: String) : Error(m)
        data class CastsNotFetched(val m: String) : Error(m)
    }
}