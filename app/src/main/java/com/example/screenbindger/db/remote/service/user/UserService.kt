package com.example.screenbindger.db.remote.service.user

import com.example.screenbindger.db.local.entity.user.observable.UserObservable

interface UserService {

    suspend fun create(user: UserObservable, userActionStateObservable: UserActionStateObservable)
    suspend fun read(user: UserObservable, userActionStateObservable: UserActionStateObservable)
    suspend fun delete(user: UserObservable, userActionStateObservable: UserActionStateObservable)
    suspend fun update(user: UserObservable, userActionStateObservable: UserActionStateObservable)
}