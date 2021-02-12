package com.example.screenbindger.db.remote.service.user

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.user.UserEntity
import com.example.screenbindger.model.state.ObjectState

class UserStateObservable(
    private val _state: MutableLiveData<ObjectState<UserEntity>>,
    private val _profilePictureUriObservable: MutableLiveData<Uri?>,
    var user: UserEntity
) {
    val value: LiveData<ObjectState<UserEntity>> = _state
    val profilePictureObservable: LiveData<Uri?> = _profilePictureUriObservable

    fun setState(state: ObjectState<UserEntity>) {
        _state.postValue(state)
    }

    fun setProfilePictureUri(uri: Uri?) {
        _profilePictureUriObservable.postValue(uri)
    }

    fun setObject(user: UserEntity) {
        this.user = user
    }
}