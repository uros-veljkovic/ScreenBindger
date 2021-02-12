package com.example.screenbindger.db.remote.response.account

import com.google.gson.annotations.SerializedName

data class RequestTokenResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("expires_at") var expiresAt: String? = null,
    @SerializedName("request_token") var requestToken: String? = null
)