package com.example.screenbindger.model.state

import com.example.screenbindger.db.remote.response.account.RequestTokenResponse

sealed class TokenState {
    object InitState : TokenState()
    data class TokenGathered(val tokenResponse: RequestTokenResponse) : TokenState()
    object Authorized : TokenState()
    data class Error(val message: Exception) : TokenState()
}