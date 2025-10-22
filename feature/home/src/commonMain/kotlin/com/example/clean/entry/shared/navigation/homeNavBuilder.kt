package com.example.clean.entry.shared.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.details.presentation.ImageDetailsRoute
import com.example.clean.entry.feed.presentation.FeedRoute

fun NavGraphBuilder.homeNavBuilder() {
    composable<AppDestination.Feed> {
        FeedRoute()
    }

    composable<AppDestination.ImageDetails> { backStackEntry ->
        val routeArgs = backStackEntry.toRoute<AppDestination.ImageDetails>()
        ImageDetailsRoute(routeArgs.imageId)
    }
}