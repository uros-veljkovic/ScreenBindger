package com.example.screenbindger.db.local.entity.user.observable

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.screenbindger.BR
import com.example.screenbindger.db.local.entity.user.UserEntity
import com.google.firebase.firestore.Exclude
import javax.inject.Inject

/**
 * The purpose of this class is make ease of observing
 * fields change and consequentially modifying entity value
 * from fragment
 */
class UserObservable
@Inject constructor() : BaseObservable() {

    @set: Exclude
    @get:Exclude
    var id: String? = null

    @get: Bindable
    var imageUri: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.imageUri)
        }

    @get: Bindable
    var fullName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.fullName)
        }

    @get: Bindable
    var email: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.email)
        }

    @set: Exclude
    @get:Exclude
    @get: Bindable
    var password: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.password)
        }

    @get: Bindable
    var dateOfBirth: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.dateOfBirth)
        }

    @set: Exclude
    @get:Exclude
    @get: Bindable
    var isLoggedIn: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.loggedIn)
        }

    fun toEntity(): UserEntity {
        return UserEntity(
            -1,
            imageUri,
            fullName,
            email,
            password,
            dateOfBirth,
            isLoggedIn
        )
    }

}