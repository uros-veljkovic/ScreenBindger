package com.example.screenbindger.db.local.entity.user.observable

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.screenbindger.BR

/**
 * The purpose of this class is make ease of observing
 * fields change and consequentially modifying entity value
 * from fragment
 */
class UserObservable: BaseObservable() {

    @get: Bindable
    var fullName: String = ""
    set(value){
        field = value
        notifyPropertyChanged(BR.fullName)
    }

    @get: Bindable
    var email: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.email)
        }

    @get: Bindable
    var password: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.password)
        }

    @get: Bindable
    var dateOfBirth: Long = 976623132000
        set(value){
            field = value
            notifyPropertyChanged(BR.dateOfBirth)
        }




}