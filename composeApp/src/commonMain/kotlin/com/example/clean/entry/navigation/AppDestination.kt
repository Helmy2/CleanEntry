package com.example.clean.entry.navigation

import kotlinx.serialization.Serializable

/**
 * A type-safe and serializable representation of all navigation destinations in the app.
 * This sealed class hierarchy defines the entire navigation graph.
 */
@Serializable
sealed class AppDestination {


    @Serializable
    data object Auth : AppDestination() {
        @Serializable
        data object Login : AppDestination()

        @Serializable
        data object Registration : AppDestination()

        @Serializable
        data class CountryCodePicker(val code: String) : AppDestination()
    }



    @Serializable
    data object Dashboard : AppDestination()
}