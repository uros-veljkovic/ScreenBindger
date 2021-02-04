package com.example.screenbindger.model.state

import android.media.session.MediaSession

sealed class TokenAuthState {
    object InitState : TokenAuthState()
    object Authorized : TokenAuthState()
    object Unauthorized : TokenAuthState()
    data class Error(val message: Exception) : TokenAuthState()
}