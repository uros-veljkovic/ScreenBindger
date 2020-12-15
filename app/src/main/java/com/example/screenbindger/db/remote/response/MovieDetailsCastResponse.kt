package com.example.screenbindger.db.remote.response

import com.example.screenbindger.model.domain.CastEntity
import com.google.gson.annotations.SerializedName

class MovieDetailsCastResponse (
    @SerializedName("cast")
    val casts: List<CastEntity>
){
}