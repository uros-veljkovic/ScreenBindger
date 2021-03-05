package com.example.screenbindger.view.fragment.register

import com.example.screenbindger.model.domain.user.UserEntity
import com.example.screenbindger.model.state.RegisterState
import com.example.screenbindger.util.event.EventObservable

class RegisterViewState : EventObservable<RegisterState<UserEntity>>()