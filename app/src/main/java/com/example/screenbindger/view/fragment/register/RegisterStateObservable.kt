package com.example.screenbindger.view.fragment.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.model.state.LoginState
import com.example.screenbindger.model.state.RegisterState
import com.example.screenbindger.util.state.StateObservable

class RegisterStateObservable : StateObservable<RegisterState<UserEntity>>()