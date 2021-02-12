package com.example.screenbindger.db.remote.response.account

import com.google.gson.annotations.SerializedName

data class ValidateRequestTokenResponse(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("expires_at") val expiresAt: String? = null,
    @SerializedName("request_token") val requestToken: String? = null
)