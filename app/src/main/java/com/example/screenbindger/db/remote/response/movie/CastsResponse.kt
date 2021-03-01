package com.example.screenbindger.db.remote.response.movie

import com.example.screenbindger.model.domain.cast.CastEntity
import com.google.gson.annotations.SerializedName

class CastsResponse (
    @SerializedName("cast")
    val casts: List<CastEntity>
)