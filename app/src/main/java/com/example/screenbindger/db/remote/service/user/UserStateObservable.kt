package com.example.screenbindger.db.remote.service.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.model.state.ObjectState

class UserStateObservable(
    private val _state: MutableLiveData<ObjectState<UserEntity>>,
    var user: UserEntity
) {
    val value: LiveData<ObjectState<UserEntity>> = _state

    fun setState(state: ObjectState<UserEntity>) {
        _state.postValue(state)
    }

    fun setObject(user: UserEntity) {
        this.user = user
    }
}