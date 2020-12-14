package com.example.screenbindger.model.domain

import com.google.gson.annotations.SerializedName

class GenreEntity constructor(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null
) {


}