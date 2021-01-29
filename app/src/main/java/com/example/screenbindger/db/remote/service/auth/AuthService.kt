package com.example.screenbindger.db.remote.service.auth

import com.example.screenbindger.db.remote.service.user.UserStateObservable

interface AuthService {

    suspend fun signIn(email: String, password: String, stateObservable: AuthStateObservable)
    suspend fun signUp(email: String, password: String, stateObservable: AuthStateObservable)
    suspend fun signOut()
    suspend fun changePassword(
        newPassword: String,
        userStateObservable: UserStateObservable
    )
}