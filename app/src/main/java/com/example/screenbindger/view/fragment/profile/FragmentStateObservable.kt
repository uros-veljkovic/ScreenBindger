package com.example.screenbindger.view.fragment.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FragmentStateObservable {

    private val _state: MutableLiveData<ProfileFragmentState> =
        MutableLiveData(ProfileFragmentState.NotEditable)
    val state: LiveData<ProfileFragmentState> = _state

    fun setState(state: ProfileFragmentState) {
        _state.postValue(state)
    }
}