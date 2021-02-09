package com.example.screenbindger.view.fragment.login

import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.model.state.AuthState
import com.example.screenbindger.util.state.StateObservable
import java.lang.Exception
import javax.inject.Inject

class AuthorizationStateObservable
@Inject constructor(

) : StateObservable<AuthState>() {

    fun getState(): AuthState =
        this.value.value ?: AuthState.Error.NoState(Exception("State error"))


}