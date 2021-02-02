package com.example.screenbindger.model.domain

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.screenbindger.BR
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import javax.inject.Inject

/**
 * The purpose of this class is make ease of observing
 * fields change and consequentially modifying entity value
 * from fragment
 */
class UserEntity
@Inject constructor() : BaseObservable() {

    @DocumentId
    @get: PropertyName("id")
    @set: PropertyName("id")
    var id: String? = null


    @get: Bindable
    @get: PropertyName("fullName")
    @set: PropertyName("fullName")
    var fullName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.fullName)
        }

    @get: Bindable
    @get: PropertyName("email")
    @set: PropertyName("email")
    var email: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.email)
        }

    @set: Exclude
    @get:Exclude
    @get: Bindable
    @get: PropertyName("password")
    @set: PropertyName("password")
    var password: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.password)
        }

    @get: Bindable
    @get: PropertyName("dateOfBirth")
    @set: PropertyName("dateOfBirth")
    var dateOfBirth: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.dateOfBirth)
        }

}