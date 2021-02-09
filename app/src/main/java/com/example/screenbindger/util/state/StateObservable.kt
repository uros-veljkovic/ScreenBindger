package com.example.screenbindger.util.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.util.event.Event

abstract class StateObservable<T>(
) {
    private val _state: MutableLiveData<T> = MutableLiveData()
    val value: LiveData<T> = _state

    fun setState(state: T) {
        _state.postValue(state)
    }
}