package com.example.screenbindger.db.remote.service.auth.firebase

import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.model.domain.UserEntity
import com.example.screenbindger.model.state.LoginState
import com.example.screenbindger.model.state.RegisterState
import com.example.screenbindger.view.fragment.login.LoginStateObservable
import com.example.screenbindger.view.fragment.register.RegisterStateObservable

interface AuthService {

    suspend fun signIn(email: String, password: String, loginStateObservable: LoginStateObservable)
    suspend fun signUp(
        email: String,
        password: String,
        registerStateObservable: RegisterStateObservable
    )

    suspend fun signOut()
    suspend fun changePassword(
        newPassword: String,
        userStateObservable: UserStateObservable
    )
}