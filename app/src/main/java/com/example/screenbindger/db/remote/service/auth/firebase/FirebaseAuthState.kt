package com.example.screenbindger.db.remote.service.auth.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.model.state.LoginState
import com.example.screenbindger.model.state.RegisterState

class FirebaseAuthState(
    private val _state: MutableLiveData<RegisterState<UserEntity>>
) {
    val value: LiveData<RegisterState<UserEntity>> = _state

    fun setValue(state: RegisterState<UserEntity>) {
        _state.postValue(state)
    }
}