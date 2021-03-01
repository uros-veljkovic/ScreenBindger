package com.example.screenbindger.view.fragment.movie_details

import com.example.screenbindger.db.remote.response.movie.trailer.TrailerDetails

sealed class MovieDetailsFragmentViewEvent {

    object Rest : MovieDetailsFragmentViewEvent()

    object IsLoadedAsFavorite : MovieDetailsFragmentViewEvent()
    object IsLoadedAsNotFavorite : MovieDetailsFragmentViewEvent()
    object Loading : MovieDetailsFragmentViewEvent()

    data class AddedToFavorites(
        val message: String = "Added to favorites :)"
    ) : MovieDetailsFragmentViewEvent()

    data class RemovedFromFavorites(
        val message: String = "Removed from favorites."
    ) : MovieDetailsFragmentViewEvent()

    data class TrailersFetched(
        val trailers: List<TrailerDetails>
    ) : MovieDetailsFragmentViewEvent()

    object TrailersNotFetched : MovieDetailsFragmentViewEvent()

    data class PosterSaved(val socialMediaRequestCode: Int) : MovieDetailsFragmentViewEvent()
    object PosterNotSaved : MovieDetailsFragmentViewEvent()

    data class Error(
        val message: String
    ) : MovieDetailsFragmentViewEvent()
}