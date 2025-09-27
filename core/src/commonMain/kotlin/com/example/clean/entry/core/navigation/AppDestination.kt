package com.example.clean.entry.core.navigation

import kotlinx.serialization.Serializable

sealed class AppDestination {
    @Serializable
    object Feed : AppDestination()

    @Serializable
    object Profile : AppDestination()

    @Serializable
    object Auth : AppDestination()

    @Serializable
    object Login : AppDestination()

    @Serializable
    object Registration : AppDestination()

    @Serializable
    data class CountryCodePicker(val code: String?) : AppDestination()
}
