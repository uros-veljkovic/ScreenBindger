package com.example.screenbindger.util.state

abstract class State<out R> {

    data class Success<out T>(val data: T? = null) : State<T>()
    data class Error(val exception: Exception) : State<Nothing>()
    object Loading : State<Nothing>()
    object Unrequested : State<Nothing>()

}

/**
 * `true` if [State] is of type [Success] & holds non-null [Success.data].
 */
val State<*>.succeeded
    get() = this is State.Success && data != null
