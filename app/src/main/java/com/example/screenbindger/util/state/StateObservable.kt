package com.example.screenbindger.util.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.model.state.RegisterState

abstract class StateObservable<T>(
) {
    private val _state: MutableLiveData<T> = MutableLiveData()
    val value: LiveData<T> = _state

    fun setValue(state: T) {
        _state.postValue(state)
    }
}