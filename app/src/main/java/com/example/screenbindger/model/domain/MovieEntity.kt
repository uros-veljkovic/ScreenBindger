package com.example.screenbindger.model.domain

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.example.screenbindger.util.constants.API_IMAGE_BASE_URL
import com.example.screenbindger.util.constants.API_KEY
import com.example.screenbindger.util.constants.POSTER_SIZE_SMALL
import com.google.gson.annotations.SerializedName

class MovieEntity : BaseObservable(){

    @SerializedName("poster_path")
    @get: Bindable
    var bigPosterUrl: String? = null
    set(value){
        field = value
        notifyPropertyChanged(BR.bigPosterUrl)
    }

    @SerializedName("backdrop_path")
    @get: Bindable
    var smallPosterUrl: String? = null
        set(value){
            field = value
            notifyPropertyChanged(BR.smallPosterUrl)
        }

    @SerializedName("title")
    @get: Bindable
    var title: String? = null
        set(value){
            field = value
            notifyPropertyChanged(BR.title)
        }

    @SerializedName("vote_average")
    @get: Bindable
    var rating: Double? = null
        set(value){
            field = value
            notifyPropertyChanged(BR.rating)
        }

    @SerializedName("overview")
    @get: Bindable
    var description: String? = null
        set(value){
            field = value
            notifyPropertyChanged(BR.description)
        }

    @SerializedName("genre_ids")
    var genreIds: List<Int>? = null
        set(value){
            field = value
        }

    @get: Bindable
    var genres: List<GenreEntity>? = null
    set(value){
        field = value
        // Preuzmi zanrove iz singleton klase
    }

    var casts: List<CastEntity?>? = null

}