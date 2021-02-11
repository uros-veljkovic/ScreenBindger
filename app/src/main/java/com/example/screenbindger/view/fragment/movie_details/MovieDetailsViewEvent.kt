package com.example.screenbindger.view.fragment.movie_details

sealed class MovieDetailsViewEvent {

    object Rest : MovieDetailsViewEvent()

    object IsLoadedAsFavorite : MovieDetailsViewEvent()
    object IsLoadedAsNotFavorite : MovieDetailsViewEvent()

    data class AddedToFavorites(
        val message: String = "Added to favorites :)"
    ) : MovieDetailsViewEvent()

    data class RemovedFromFavorites(
        val message: String = "Removed from favorites."
    ) : MovieDetailsViewEvent()

    data class Error(
        val message: String
    ) : MovieDetailsViewEvent()
}