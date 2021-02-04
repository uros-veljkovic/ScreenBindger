package com.example.screenbindger.db.remote.service.auth.firebase

import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.view.fragment.login.AuthorizationStateObservable
import com.example.screenbindger.view.fragment.register.RegisterStateObservable

interface AuthService {

    suspend fun signIn(
        email: String,
        password: String,
        authStateObservable: AuthorizationStateObservable
    )

    suspend fun signUp(
        email: String,
        password: String,
        authStateObservable: AuthorizationStateObservable
    )

    suspend fun signOut()
    suspend fun changePassword(
        newPassword: String,
        userStateObservable: UserStateObservable
    )
}