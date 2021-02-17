package com.example.screenbindger.model.domain.review

import com.google.gson.annotations.SerializedName

data class ReviewAuthorEntity(
    @SerializedName("avatar_path")
    val avatarPath: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("rating")
    val rating: Any? = null,
    @SerializedName("username")
    val username: String? = null
)