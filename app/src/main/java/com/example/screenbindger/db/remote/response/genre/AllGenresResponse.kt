package com.example.screenbindger.db.remote.response.genre

import com.example.screenbindger.model.domain.genre.GenreEntity
import com.google.gson.annotations.SerializedName

class AllGenresResponse (
    @SerializedName("genres")
    val list: List<GenreEntity>
)