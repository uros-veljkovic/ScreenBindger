package com.example.screenbindger.db.remote.service.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.util.state.State
import com.google.firebase.auth.FirebaseUser

class UserActionStateObservable(
    private val _state: MutableLiveData<UserActionState>
) {
    val value: LiveData<UserActionState> = _state

    fun setValue(state: UserActionState) {
        _state.postValue(state)
    }
}