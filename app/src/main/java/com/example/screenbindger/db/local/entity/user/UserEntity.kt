package com.example.screenbindger.db.local.entity.user

import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.screenbindger.BR
import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @DocumentId
    var id: String? = null,
    var imageUri: String? = null,
    var fullName: String = "",
    var email: String = "",
    @set: Exclude
    @get:Exclude
    var password: String = "",
    var dateOfBirth: String? = null
) {
    fun toObservable(): UserObservable {
        return UserObservable().also {
            it.id = id
            it.imageUri = imageUri
            it.fullName = fullName
            it.email = email
            it.dateOfBirth = dateOfBirth
            it.password = password
        }
    }
}