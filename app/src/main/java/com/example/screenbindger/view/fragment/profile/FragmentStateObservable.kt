package com.example.screenbindger.view.fragment.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FragmentStateObservable {

    private val _state: MutableLiveData<ProfileViewState> =
        MutableLiveData(ProfileViewState.NotEditable)
    val state: LiveData<ProfileViewState> = _state

    fun setState(state: ProfileViewState) {
        _state.postValue(state)
    }
}