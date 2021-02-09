package com.example.screenbindger.util.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.model.state.RegisterState
import com.example.screenbindger.util.event.Event

abstract class StateObservable<T>(
) {
    private val _state: MutableLiveData<Event<T>> = MutableLiveData()
    val value: LiveData<Event<T>> = _state

    fun setState(state: T) {
        _state.postValue(Event(state))
    }
}