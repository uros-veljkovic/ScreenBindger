package com.example.screenbindger.db.remote.request

import com.google.gson.annotations.SerializedName

data class ValidateRequestTokenRequestBody(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("request_token") val requestToken: String
)