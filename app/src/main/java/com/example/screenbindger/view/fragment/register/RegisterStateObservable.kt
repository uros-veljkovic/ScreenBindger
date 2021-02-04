package com.example.screenbindger.view.fragment.register

import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.model.state.RegisterState
import com.example.screenbindger.util.state.StateObservable

class RegisterStateObservable : StateObservable<RegisterState<UserEntity>>()