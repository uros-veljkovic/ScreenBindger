package com.example.screenbindger.model.domain

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.example.screenbindger.model.enums.ItemType
import com.google.gson.annotations.SerializedName

class CastEntity : BaseObservable(), Item {

    @SerializedName("profile_path")
    @get: Bindable
    var imageUrl: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.imageUrl)
        }

    @SerializedName("character")
    @get: Bindable
    var movieName: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.movieName)
        }


    @SerializedName("original_name")
    @get: Bindable
    var realName: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.realName)
        }

    override fun getItemType(): ItemType = ItemType.CAST
}