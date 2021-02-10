package com.example.screenbindger.db.remote.response

import com.example.screenbindger.model.domain.GenreEntity
import com.google.gson.annotations.SerializedName

class AllGenresResponse (
    @SerializedName("genres")
    val list: List<GenreEntity>
)