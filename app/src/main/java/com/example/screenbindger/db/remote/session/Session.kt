package com.example.screenbindger.db.remote.session

data class Session(
    var success: Boolean? = null,
    var expiresAt: String? = null,
    var requestToken: String? = null,
    var sessionId: String? = null
) {
    fun getAuthorizationUrl(): String =
        "https://www.themoviedb.org/authenticate/$requestToken"

}