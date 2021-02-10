package com.example.screenbindger.db.remote.response

import com.google.gson.annotations.SerializedName

data class MarkAsFavoriteResponse(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("status_message") val statusMessage: String
)