package com.example.screenbindger.view.fragment.details

import com.example.screenbindger.db.remote.response.movie.trailer.TrailerDetails

sealed class DetailsViewEvent {

    object Rest : DetailsViewEvent()

    object IsLoadedAsFavorite : DetailsViewEvent()
    object IsLoadedAsNotFavorite : DetailsViewEvent()
    object Loading : DetailsViewEvent()

    data class AddedToFavorites(
        val message: String = "Added to favorites :)"
    ) : DetailsViewEvent()

    data class RemovedFromFavorites(
        val message: String = "Removed from favorites."
    ) : DetailsViewEvent()

    data class TrailersFetched(
        val trailers: List<TrailerDetails>
    ) : DetailsViewEvent()

    object TrailersNotFetched : DetailsViewEvent()

    data class PosterSaved(val socialMediaRequestCode: Int) : DetailsViewEvent()
    object PosterNotSaved : DetailsViewEvent()

    data class Error(
        val message: String
    ) : DetailsViewEvent()
}