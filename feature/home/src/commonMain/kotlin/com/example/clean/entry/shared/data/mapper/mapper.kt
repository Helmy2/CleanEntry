package com.example.clean.entry.shared.data.mapper

import androidx.compose.ui.graphics.Color
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
        aspectRatio = width?.toFloat()?.div(height?.toFloat() ?: 1f) ?: 1f,
        avgColor = avgColor?.toComposeColor()
    )
}

private fun String.toComposeColor(): Color {
    val cleanHex = if (this.startsWith("#")) this.substring(1) else this

    if (cleanHex.length != 6 && cleanHex.length != 8) {
        return Color.Black
    }

    return try {
        val colorLong = cleanHex.toLong(16)

        if (cleanHex.length == 8) { // ARGB
            val alpha = (colorLong shr 24 and 0xFF).toInt()
            val red = (colorLong shr 16 and 0xFF).toInt()
            val green = (colorLong shr 8 and 0xFF).toInt()
            val blue = (colorLong and 0xFF).toInt()
            Color(red, green, blue, alpha)
        } else { // RGB
            val red = (colorLong shr 16 and 0xFF).toInt()
            val green = (colorLong shr 8 and 0xFF).toInt()
            val blue = (colorLong and 0xFF).toInt()
            Color(red, green, blue)
        }
    } catch (_: NumberFormatException) {
        Color.Black
    }
}