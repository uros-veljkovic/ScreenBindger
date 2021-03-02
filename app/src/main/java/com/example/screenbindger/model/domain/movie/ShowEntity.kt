package com.example.screenbindger.model.domain.movie

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.example.screenbindger.R
import com.example.screenbindger.model.domain.Item
import com.example.screenbindger.model.domain.genre.GenreEntity
import com.example.screenbindger.model.enums.ItemType
import com.example.screenbindger.model.global.Genres
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShowEntity : BaseObservable(), Item {

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("backdrop_path")
    @get: Bindable
    var bigPosterUrl: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.bigPosterUrl)
        }

    val backdropPathPlaceholder = R.drawable.video_player

    @SerializedName("poster_path")
    @get: Bindable
    var smallPosterUrl: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.smallPosterUrl)
        }

    @SerializedName(value = "show_title", alternate = ["title", "name"])
    @get: Bindable
    var title: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }

    @SerializedName("vote_average")
    @get: Bindable
    var rating: Double? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.rating)
        }

    @SerializedName("overview")
    @get: Bindable
    var description: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.description)
        }

    @SerializedName("genre_ids")
    var genreIds: List<Int>? = null
        set(value) {
            field = value
        }

    @SerializedName("genres")
    @get: Bindable
    var genres: List<GenreEntity>? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.genres)
        }

    @get: Bindable
    var genresString: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.genresString)
        }

    fun generateGenreString() {
        genres?.forEach { genre ->
            genresString += genre.name + ", "
        }
        genresString = genresString.dropLast(2)
    }

    override fun getItemType(): ItemType = ItemType.MOVIE_DETAILS

}

fun List<ShowEntity>.generateGenres() {
    CoroutineScope(Dispatchers.Default).launch {
        forEach { movie ->
            movie.genreIds?.forEach { generId ->
                Genres.list.forEach { concreteGenre ->
                    if (concreteGenre.id == generId &&
                        movie.genresString.contains(concreteGenre.name!!).not()
                    ) {
                        movie.genresString += "${concreteGenre.name}, "
                    }
                }
            }
            movie.genresString = movie.genresString.dropLast(2)
        }
    }
}