package com.example.screenbindger.db.remote.service.auth

interface AuthService {

    suspend fun signIn(email: String, password: String, stateObservable: AuthStateObservable)
    suspend fun signUp(email: String, password: String, stateObservable: AuthStateObservable)
    suspend fun signOut()
}