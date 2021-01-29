package com.example.screenbindger.db.remote.service.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import com.example.screenbindger.model.state.ObjectState

class UserStateObservable(
    private val _state: MutableLiveData<ObjectState<UserObservable>>,
    var user: UserObservable
) {
    val value: LiveData<ObjectState<UserObservable>> = _state

    fun setState(state: ObjectState<UserObservable>) {
        _state.postValue(state)
    }

    fun setObject(user: UserObservable) {
        this.user = user
    }
}