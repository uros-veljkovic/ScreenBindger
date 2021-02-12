package com.example.screenbindger.db.remote.response.account

import com.google.gson.annotations.SerializedName

data class CreateSessionResponse(
    @SerializedName("success") var success: Boolean,
    @SerializedName("session_id") var sessionId: String
)