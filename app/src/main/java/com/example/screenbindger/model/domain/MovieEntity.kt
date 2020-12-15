package com.example.screenbindger.model.domain

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.example.screenbindger.model.global.Genres
import com.google.gson.annotations.SerializedName

class MovieEntity : BaseObservable() {

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
    set(value){
        field = value
        notifyPropertyChanged(BR.genres)
    }

    @get: Bindable
    var genresString: String = ""
    set(value){
        field = value
        notifyPropertyChanged(BR.genresString)
    }

    var casts: List<CastEntity?>? = null

}