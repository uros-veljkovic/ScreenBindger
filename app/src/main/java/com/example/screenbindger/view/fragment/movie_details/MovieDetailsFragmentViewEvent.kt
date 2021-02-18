package com.example.screenbindger.view.fragment.movie_details

sealed class MovieDetailsFragmentViewEvent {

    object Rest : MovieDetailsFragmentViewEvent()

    object IsLoadedAsFavorite : MovieDetailsFragmentViewEvent()
    object IsLoadedAsNotFavorite : MovieDetailsFragmentViewEvent()

    data class AddedToFavorites(
        val message: String = "Added to favorites :)"
    ) : MovieDetailsFragmentViewEvent()

    data class RemovedFromFavorites(
        val message: String = "Removed from favorites."
    ) : MovieDetailsFragmentViewEvent()

    data class Error(
        val message: String
    ) : MovieDetailsFragmentViewEvent()
}