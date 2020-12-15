package com.example.screenbindger.model.domain

import com.example.screenbindger.model.enums.ItemType
import com.google.gson.annotations.SerializedName

class CastEntity constructor(
    @SerializedName("profile_path")
    val imageUrl: String?,
    @SerializedName("character")
    val movieName: String?,
    @SerializedName("original_name")
    val realName: String?
): Item{
    override fun getItemType(): ItemType = ItemType.CAST
}