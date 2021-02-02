package com.example.screenbindger.db.remote.service.user

interface UserService {

    suspend fun create(userStateObservable: UserStateObservable)
    suspend fun read(userStateObservable: UserStateObservable)
    suspend fun delete(userStateObservable: UserStateObservable)
    suspend fun update(userStateObservable: UserStateObservable)
}