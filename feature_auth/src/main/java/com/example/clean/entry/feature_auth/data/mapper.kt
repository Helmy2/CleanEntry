package com.example.clean.entry.feature_auth.data

import com.example.clean.entry.feature_auth.CountriesQuery
import com.example.clean.entry.feature_auth.domain.model.Country


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