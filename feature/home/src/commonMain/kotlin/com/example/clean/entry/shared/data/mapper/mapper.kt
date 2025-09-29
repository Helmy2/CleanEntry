package com.example.clean.entry.shared.data.mapper

import com.example.clean.entry.shared.data.models.PexelsPhoto
import com.example.clean.entry.shared.domain.model.Image

fun PexelsPhoto.toDomainModel(): Image {
    return Image(
        id = id,
        url = url.orEmpty(),
        photographer = photographer.orEmpty(),
        large = src?.large2x ?: src?.large ?: src?.original ?: "",
        medium = src?.medium ?: src?.large ?: src?.original ?: "",
        small = src?.small ?: src?.medium ?: src?.large ?: src?.original ?: "",
        alt = alt.orEmpty(),
        aspectRatio = width?.toFloat()?.div(height?.toFloat() ?: 1f) ?: 1f
    )
}