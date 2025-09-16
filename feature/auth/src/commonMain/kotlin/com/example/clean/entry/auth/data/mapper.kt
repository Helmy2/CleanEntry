package com.example.clean.entry.auth.data

import com.example.clean.entry.CountriesQuery
import com.example.clean.entry.auth.domain.model.Country
import com.example.clean.entry.db.CountryEntity


/**
 * Maps the Apollo-generated Country model to our domain Country model.
 */
fun CountriesQuery.Country.toCountry(): Country {
    return Country(
        name = name,
        dialCode = "+${phone}",
        code = code,
        flagEmoji = emoji
    )
}

/**
 * Maps our domain Country model to the Room database CountryEntity.
 */
fun Country.toEntity(): CountryEntity {
    return CountryEntity(
        code = this.code,
        name = this.name,
        dialCode = this.dialCode,
        flagEmoji = this.flagEmoji
    )
}

/**
 * Converts this database entity to a clean domain model.
 */
fun CountryEntity.toCountry(): Country {
    return Country(
        name = name,
        dialCode = dialCode,
        code = code,
        flagEmoji = flagEmoji
    )
}