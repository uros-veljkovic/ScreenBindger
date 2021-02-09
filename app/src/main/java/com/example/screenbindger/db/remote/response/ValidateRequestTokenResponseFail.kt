package com.example.screenbindger.db.remote.response

import com.google.gson.annotations.SerializedName

data class ValidateRequestTokenResponseFail(
    @SerializedName("status_message") val statusMessage: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("status_code") val statusCode: String
)