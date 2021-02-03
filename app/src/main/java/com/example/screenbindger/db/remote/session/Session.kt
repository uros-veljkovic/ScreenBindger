package com.example.screenbindger.db.remote.session

import com.example.screenbindger.model.domain.UserEntity
import java.lang.Exception

data class Session(
    var user: UserEntity,
    var success: Boolean? = null,
    var expiresAt: String? = null,
    var requestToken: String? = null,
    var id: String? = null
) {
    fun getAuthorizationUrl(): String {
        if (requestToken.isNullOrEmpty().not())
            return "https://www.themoviedb.org/authenticate/$requestToken"
        else throw Exception("User not authorized")
    }

}