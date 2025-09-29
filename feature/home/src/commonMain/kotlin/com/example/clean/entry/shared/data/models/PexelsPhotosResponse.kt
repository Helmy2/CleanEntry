package com.example.clean.entry.shared.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PexelsPhotosResponse(
    val page: Int,
    @SerialName("per_page")
    val perPage: Int,
    val photos: List<PexelsPhoto>,
    @SerialName("total_results")
    val totalResults: Int,
    @SerialName("next_page")
    val nextPage: String? = null
)

@Serializable
data class PexelsPhoto(
    val id: Long,
    val width: Int?,
    val height: Int?,
    val url: String?,
    val photographer: String?,
    @SerialName("photographer_url")
    val photographerUrl: String?,
    @SerialName("photographer_id")
    val photographerId: Long?,
    @SerialName("avg_color")
    val avgColor: String?,
    val src: PexelsPhotoSrc?,
    val liked: Boolean?,
    val alt: String?
)

@Serializable
data class PexelsPhotoSrc(
    val original: String?,
    val large2x: String?,
    val large: String?,
    val medium: String?,
    val small: String?,
    val portrait: String?,
    val landscape: String?,
    val tiny: String?
)