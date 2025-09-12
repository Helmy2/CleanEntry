package com.example.clean.entry.feature.auth.navigation

import kotlinx.serialization.Serializable

sealed class AuthDestination {

    @Serializable
    data object Login : AuthDestination()

    @Serializable
    data object Registration : AuthDestination()

    @Serializable
    data class CountryCodePicker(val code: String) : AuthDestination()
}