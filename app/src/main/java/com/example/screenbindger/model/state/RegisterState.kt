package com.example.screenbindger.model.state

import java.lang.Exception

sealed class RegisterState<out R> {
    data class Success<out T>(val data: T? = null) : RegisterState<T>()
    data class Error(val exception: Exception) : RegisterState<Nothing>()
    object Loading : RegisterState<Nothing>()
    object Rest : RegisterState<Nothing>()
}