package com.example.clean.entry.home.data.mapper

import com.example.clean.entry.home.data.models.PexelsPhoto
import com.example.clean.entry.home.domain.model.Image

fun PexelsPhoto.toDomainModel(): Image {
    return Image(
        id = id,
        url = url.orEmpty(),
        photographer = photographer.orEmpty(),
        imageUrl = src?.large.orEmpty(),
        aspectRatio = width?.toFloat()?.div(height?.toFloat() ?: 1f) ?: 1f
    )
}