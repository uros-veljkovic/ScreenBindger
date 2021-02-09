package com.example.screenbindger.util.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class EventObservable<T>(
) {
    private val _state: MutableLiveData<Event<T>> = MutableLiveData()
    val value: LiveData<Event<T>> = _state

    fun setState(state: T) {
        _state.postValue(Event(state))
    }
}