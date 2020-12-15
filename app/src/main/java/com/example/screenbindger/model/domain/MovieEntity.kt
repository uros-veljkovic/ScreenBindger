package com.example.screenbindger.model.domain

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.example.screenbindger.model.enums.ItemType
import com.google.gson.annotations.SerializedName

class MovieEntity : BaseObservable(), Item {

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("poster_path")
    @get: Bindable
    var bigPosterUrl: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.bigPosterUrl)
        }

    @SerializedName("backdrop_path")
    @get: Bindable
    var smallPosterUrl: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.smallPosterUrl)
        }

    @SerializedName("title")
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

    override fun getItemType(): ItemType = ItemType.MOVIE_DETAILS

}