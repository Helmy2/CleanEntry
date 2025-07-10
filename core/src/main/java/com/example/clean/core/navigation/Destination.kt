package com.example.clean.core.navigation

import kotlinx.serialization.Serializable

/**
 * A type-safe and serializable representation of all navigation destinations in the app.
 * This sealed class hierarchy defines the entire navigation graph.
 */
@Serializable
sealed class Destination {

    @Serializable
    data object AuthGraph : Destination()

    @Serializable
    data object Login : Destination()

    @Serializable
    data object Registration : Destination()

    @Serializable
    data class CountryCodePicker(val code: String) : Destination()

    @Serializable
    data object MainGraph : Destination()

    @Serializable
    data object Dashboard : Destination()
}