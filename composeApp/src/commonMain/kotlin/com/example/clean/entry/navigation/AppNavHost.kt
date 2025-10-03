package com.example.clean.entry.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.clean.entry.auth.navigation.authNavBuilder
import com.example.clean.entry.auth.presentation.profile.ProfileRoute
import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.core.navigation.AppNavigator
import com.example.clean.entry.core.navigation.Command
import com.example.clean.entry.core.util.ObserveEffect
import com.example.clean.entry.details.presentation.ImageDetailsScreen
import com.example.clean.entry.feed.presentation.FeedRoute
import org.koin.compose.koinInject

private sealed class BottomNavItem(
    val destination: AppDestination,
    val label: String,
    val icon: ImageVector
) {
    companion object {
        val allItems = listOf(Feed, Profile)
    }

    data object Feed : BottomNavItem(AppDestination.Feed, "Feed", Icons.Default.Home)
    data object Profile :
        BottomNavItem(AppDestination.Profile, "Profile", Icons.Default.AccountCircle)
}

@Composable
fun AppNavHost(
    startDestination: AppDestination,
    onNavHostReady: suspend (NavController) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val navigator = koinInject<AppNavigator>()

    ObserveEffect(navigator.commands) {
        navigator.commands.collect { command ->
            when (command) {
                is Command.NavigateAsRoot -> {
                    navController.navigate(command.destination) {
                        popUpTo(0)
                    }
                }

                Command.NavigateBack -> navController.popBackStack()
                is Command.NavigateTo -> navController.navigate(command.destination)
            }
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showNavigationUi =
        BottomNavItem.allItems.any { currentDestination?.hasRoute(it.destination::class) == true }

    NavigationSuiteScaffold(
        modifier = modifier,
        layoutType = if (showNavigationUi) {
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                currentWindowAdaptiveInfo()
            )
        } else {
            NavigationSuiteType.None
        },
        navigationSuiteItems = {
            BottomNavItem.allItems.forEach { item ->
                item(
                    selected = navBackStackEntry?.destination?.hierarchy?.any { it.hasRoute(item.destination::class) } == true,
                    onClick = {
                        navController.navigate(item.destination) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) }
                )
            }
        }, content = {
            AppNavHostContent(
                navController,
                startDestination,
            )
        }
    )
    LaunchedEffect(navController) {
        onNavHostReady(navController)
    }
}

@Composable
private fun AppNavHostContent(
    navController: NavHostController,
    startDestination: AppDestination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        authNavBuilder()

        composable<AppDestination.Feed> {
            FeedRoute()
        }
        composable<AppDestination.Profile> {
            ProfileRoute()
        }
        composable<AppDestination.ImageDetails> { backStackEntry ->
            val routeArgs = backStackEntry.toRoute<AppDestination.ImageDetails>()
            ImageDetailsScreen(routeArgs.imageId)
        }
    }
}
