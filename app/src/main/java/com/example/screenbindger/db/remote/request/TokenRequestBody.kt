package com.example.screenbindger.db.remote.request

import com.google.gson.annotations.SerializedName

data class TokenRequestBody(
    @SerializedName("request_token") val requestToken: String
)