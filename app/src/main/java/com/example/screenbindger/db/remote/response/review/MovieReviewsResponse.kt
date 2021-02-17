package com.example.screenbindger.db.remote.response.review
import com.example.screenbindger.model.domain.review.ReviewEntity
import com.google.gson.annotations.SerializedName

data class MovieReviewsResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("results")
    val results: List<ReviewEntity>? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null
)