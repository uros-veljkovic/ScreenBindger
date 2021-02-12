package com.example.screenbindger.db.remote.session

import com.example.screenbindger.model.domain.user.UserEntity
import java.lang.Exception

data class Session(
    var id: String? = null,
    var accountId: Int? = null,
    var user: UserEntity? = null,
    var success: Boolean? = null,
    var expiresAt: String? = null,
    var requestToken: String? = null
) {
    fun getAuthorizationUrl(): String {
        if (requestToken.isNullOrEmpty().not())
            return "https://www.themoviedb.org/authenticate/$requestToken"
        else throw Exception("User not authorized")
    }

}