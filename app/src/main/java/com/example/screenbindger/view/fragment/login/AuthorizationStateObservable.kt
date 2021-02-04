package com.example.screenbindger.view.fragment.login

import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.model.state.AuthState
import com.example.screenbindger.util.state.StateObservable
import javax.inject.Inject

class AuthorizationStateObservable
@Inject constructor(
    
) : StateObservable<AuthState<UserEntity>>()