package com.example.screenbindger.model.state

sealed class ObjectState<out R> {

    data class Created<out T>(val data: T? = null) : ObjectState<T>()
    data class Read<out T>(val data: T? = null) : ObjectState<T>()
    data class Updated<out T>(val data: T? = null) : ObjectState<T>()
    data class Deleted<out T>(val data: T? = null) : ObjectState<T>()
    data class InitialState<out T>(val data: T? = null) : ObjectState<Nothing>()
    data class Error(val exception: Exception) : ObjectState<Nothing>()

}