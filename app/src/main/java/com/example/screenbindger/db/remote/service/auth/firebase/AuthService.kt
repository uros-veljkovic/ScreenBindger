package com.example.screenbindger.db.remote.service.auth.firebase

import com.example.screenbindger.db.remote.service.user.UserStateObservable
import com.example.screenbindger.view.fragment.login.AuthorizationEventObservable

interface AuthService {

    suspend fun signIn(
        email: String,
        password: String,
        authStateObservable: AuthorizationEventObservable
    )

    suspend fun signUp(
        email: String,
        password: String,
        authStateObservable: AuthorizationEventObservable
    )

    suspend fun signOut()
    suspend fun changePassword(
        newPassword: String,
        userStateObservable: UserStateObservable
    )
}