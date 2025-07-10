package com.example.clean.entry.feature_auth.domain.model

/**
 * Represents a country with its essential information for the picker.
 */
data class Country(
    val name: String,
    val dialCode: String,
    val code: String, // ISO 3166-1 alpha-2 code (e.g., "EG")
    val flagEmoji: String
)
