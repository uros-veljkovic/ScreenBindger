package com.example.screenbindger.db.remote.service.user

import com.example.screenbindger.db.local.entity.user.observable.UserObservable
import com.example.screenbindger.model.state.ObjectState

interface UserService {

    suspend fun create(userStateObservable: UserStateObservable)
    suspend fun read(userStateObservable: UserStateObservable)
    suspend fun delete(userStateObservable: UserStateObservable)
    suspend fun update(userStateObservable: UserStateObservable)
}