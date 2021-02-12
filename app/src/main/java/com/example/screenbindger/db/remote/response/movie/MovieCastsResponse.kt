package com.example.screenbindger.db.remote.response.movie

import com.example.screenbindger.model.domain.CastEntity
import com.google.gson.annotations.SerializedName

class MovieCastsResponse (
    @SerializedName("cast")
    val casts: List<CastEntity>
)