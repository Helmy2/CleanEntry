package com.example.clean.entry.feature_auth.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class CountryData(
    val name: String,
    val dialCode: String,
    val code: String,
    val flagEmoji: String
): Parcelable


fun CountryData.toCountry() = Country(
    name = name,
    dialCode = dialCode,
    code = code,
    flagEmoji = flagEmoji
)