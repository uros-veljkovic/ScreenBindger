package com.example.screenbindger.view.fragment.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FragmentStateObservable {

    private val _state: MutableLiveData<FragmentState> =
        MutableLiveData(FragmentState.NotEditable)
    val state: LiveData<FragmentState> = _state

    fun setState(state: FragmentState) {
        _state.postValue(state)
    }
}