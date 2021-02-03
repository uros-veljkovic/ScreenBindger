package com.example.screenbindger.model.state

import java.lang.Exception

sealed class LoginState<out R> {
    data class Success<out T>(val data: T? = null) : LoginState<T>()
    data class Error(val exception: Exception) : LoginState<Nothing>()
    object Loading : LoginState<Nothing>()
    object Rest : LoginState<Nothing>()
}