package com.example.clean.entry.navigation

import kotlinx.serialization.Serializable

/**
 * A type-safe and serializable representation of all navigation destinations in the app.
 * This sealed class hierarchy defines the entire navigation graph.
 */
@Serializable
sealed class AppDestination {

    @Serializable
    data object AuthGraph : AppDestination()

    @Serializable
    data object MainGraph : AppDestination()

    @Serializable
    data object Dashboard : AppDestination()
}