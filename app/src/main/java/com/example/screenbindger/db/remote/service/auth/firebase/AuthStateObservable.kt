package com.example.screenbindger.db.remote.service.auth.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.util.state.State
import com.google.firebase.auth.FirebaseUser

class AuthStateObservable(
    private val _state: MutableLiveData<State<FirebaseUser>>
) {
    val value: LiveData<State<FirebaseUser>> = _state

    fun setValue(state: State<FirebaseUser>) {
        _state.postValue(state)
    }
}