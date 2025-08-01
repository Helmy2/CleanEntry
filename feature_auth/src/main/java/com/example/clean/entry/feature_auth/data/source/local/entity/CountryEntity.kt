package com.example.clean.entry.feature_auth.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents the structure of the 'countries' table in the Room database.
 *
 * @property code The ISO 3166-1 alpha-2 country code (e.g., "EG"). This is the primary key.
 * @property name The full name of the country (e.g., "Egypt").
 * @property dialCode The international dialing code (e.g., "+20").
 * @property flagEmoji The emoji representing the country's flag.
 */
@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey
    val code: String,
    val name: String,
    val dialCode: String,
    val flagEmoji: String
)
