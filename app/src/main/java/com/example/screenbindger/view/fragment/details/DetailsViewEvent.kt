package com.example.screenbindger.view.fragment.details

import com.example.screenbindger.R
import com.example.screenbindger.db.remote.response.movie.trailer.TrailerDetails

sealed class DetailsViewEvent
object Rest : DetailsViewEvent()
object Loading : DetailsViewEvent()

object MarkedAsFavorite : DetailsViewEvent()
object MarkedAsNotFavorite : DetailsViewEvent()

data class TrailersFetched(
    val trailers: List<TrailerDetails>
) : DetailsViewEvent()

data class TrailersNotFetched(
    val messageStringResId: Int = R.string.trailers_not_fetched
) : DetailsViewEvent()

data class PosterSaved(val socialMediaRequestCode: Int) : DetailsViewEvent()
data class PosterNotSaved(
    val messageStringResId: Int = R.string.error_sharing_poster
) : DetailsViewEvent()

data class NetworkError(
    val messageStringResId: Int
) : DetailsViewEvent()
