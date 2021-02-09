package com.example.screenbindger.db.remote.response

import com.google.gson.annotations.SerializedName

data class AccountDetailsResponse(
    @SerializedName("id") val accountId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String
)