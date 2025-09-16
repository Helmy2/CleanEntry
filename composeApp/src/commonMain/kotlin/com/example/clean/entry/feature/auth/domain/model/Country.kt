package com.example.clean.entry.feature.auth.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a country with its essential information for the picker.
 */
@Serializable
data class Country(
    val name: String,
    val dialCode: String,
    val code: String, // ISO 3166-1 alpha-2 code (e.g., "EG")
    val flagEmoji: String
){
    companion object {
        val Egypt = Country(
            name = "Egypt",
            dialCode = "+20",
            code = "EG",
            flagEmoji = "🇪🇬"
        )
    }
}
