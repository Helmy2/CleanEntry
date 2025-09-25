package com.example.clean.entry.feed.domain.model

data class Image(
    val id: Long,
    val url: String,
    val photographer: String,
    val imageUrl: String,
    val aspectRatio: Float,
)