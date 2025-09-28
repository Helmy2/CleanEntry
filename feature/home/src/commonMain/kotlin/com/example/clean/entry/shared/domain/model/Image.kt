package com.example.clean.entry.shared.domain.model

data class Image(
    val id: Long,
    val url: String,
    val photographer: String,
    val aspectRatio: Float,
    val alt: String,
    val large: String,
    val medium: String,
    val small: String,
)