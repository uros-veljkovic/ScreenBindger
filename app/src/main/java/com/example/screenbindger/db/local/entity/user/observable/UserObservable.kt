package com.example.screenbindger.db.local.entity.user.observable

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.screenbindger.BR
import com.example.screenbindger.db.local.entity.user.UserEntity

/**
 * The purpose of this class is make ease of observing
 * fields change and consequentially modifying entity value
 * from fragment
 */
class UserObservable : BaseObservable() {

    var id: Int? = null

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

    @get: Bindable
    var isLoggedIn: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.loggedIn)
        }

    fun toEntity(): UserEntity {
        return UserEntity(
            id,
            imageUri,
            fullName,
            email,
            password,
            dateOfBirth,
            isLoggedIn
        )
    }

}