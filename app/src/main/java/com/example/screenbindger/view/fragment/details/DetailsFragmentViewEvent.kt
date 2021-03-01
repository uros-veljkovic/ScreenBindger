package com.example.screenbindger.view.fragment.details

import com.example.screenbindger.db.remote.response.movie.trailer.TrailerDetails

sealed class DetailsFragmentViewEvent {

    object Rest : DetailsFragmentViewEvent()

    object IsLoadedAsFavorite : DetailsFragmentViewEvent()
    object IsLoadedAsNotFavorite : DetailsFragmentViewEvent()
    object Loading : DetailsFragmentViewEvent()

    data class AddedToFavorites(
        val message: String = "Added to favorites :)"
    ) : DetailsFragmentViewEvent()

    data class RemovedFromFavorites(
        val message: String = "Removed from favorites."
    ) : DetailsFragmentViewEvent()

    data class TrailersFetched(
        val trailers: List<TrailerDetails>
    ) : DetailsFragmentViewEvent()

    object TrailersNotFetched : DetailsFragmentViewEvent()

    data class PosterSaved(val socialMediaRequestCode: Int) : DetailsFragmentViewEvent()
    object PosterNotSaved : DetailsFragmentViewEvent()

    data class Error(
        val message: String
    ) : DetailsFragmentViewEvent()
}