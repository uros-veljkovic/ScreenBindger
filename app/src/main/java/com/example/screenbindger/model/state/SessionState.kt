package com.example.screenbindger.model.state

import com.example.screenbindger.db.remote.response.RequestTokenResponse
import com.example.screenbindger.db.remote.session.Session

sealed class SessionState {
    object InitState : SessionState()
    data class SessionStarted(val session: Session) : SessionState()
    data class Error(val message: Exception) : SessionState()
}