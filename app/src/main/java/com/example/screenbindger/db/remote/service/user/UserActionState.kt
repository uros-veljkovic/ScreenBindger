package com.example.screenbindger.db.remote.service.user

sealed class UserActionState {

    object Created : UserActionState()
    object Read : UserActionState()
    object Updated : UserActionState()
    object Deleted : UserActionState()
    object Unrequested : UserActionState()
    data class Failed(val exception: Exception) : UserActionState()

}