package com.example.clean.entry.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.window.core.layout.WindowSizeClass
import com.example.clean.entry.auth.navigation.authNavBuilder
import com.example.clean.entry.auth.presentation.profile.ProfileRoute
import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.core.navigation.AppNavigator
import com.example.clean.entry.core.navigation.Command
import com.example.clean.entry.details.presentation.ImageDetailsScreen
import com.example.clean.entry.feed.presentation.FeedRoute
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

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
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val navigator = koinInject<AppNavigator>()

    LaunchedEffect(navigator.commands) {
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

    val adaptiveInfo = currentWindowAdaptiveInfo()
    val isWidthAtLeastBreakpoint =
        adaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)

    if (isWidthAtLeastBreakpoint) {
        ExpandedAppLayout(
            modifier = modifier,
            navController = navController,
            bottomNavItems = BottomNavItem.allItems,
            startDestination = startDestination,
            showNavigationRail = showNavigationUi,
            isItemSelected = { item ->
                navBackStackEntry?.destination?.hierarchy?.any { it.hasRoute(item.destination::class) } == true
            }
        )
    } else {
        CompactAppLayout(
            modifier = modifier,
            navController = navController,
            bottomNavItems = BottomNavItem.allItems,
            startDestination = startDestination,
            showBottomBar = showNavigationUi,
            isItemSelected = { item ->
                navBackStackEntry?.destination?.hierarchy?.any { it.hasRoute(item.destination::class) } == true
            }
        )
    }
}

@Composable
private fun CompactAppLayout(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    bottomNavItems: List<BottomNavItem>,
    startDestination: AppDestination,
    showBottomBar: Boolean,
    isItemSelected: (BottomNavItem) -> Boolean
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = isItemSelected(item),
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
                }
            }
        }
    ) { innerPadding ->
        AppNavHostContent(navController, startDestination, Modifier.padding(innerPadding))
    }
}

@Composable
private fun ExpandedAppLayout(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    bottomNavItems: List<BottomNavItem>,
    startDestination: AppDestination,
    showNavigationRail: Boolean,
    isItemSelected: (BottomNavItem) -> Boolean
) {
    Scaffold(modifier = modifier) { scaffoldPadding ->
        Row(
            Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            if (showNavigationRail) {
                NavigationRail {
                    bottomNavItems.forEach { item ->
                        NavigationRailItem(
                            selected = isItemSelected(item),
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
                            label = { Text(item.label) },
                            alwaysShowLabel = true
                        )
                    }
                }
            }
            AppNavHostContent(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.weight(1f)
            )
        }
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
            ImageDetailsScreen(viewModel = koinViewModel { parametersOf(routeArgs.imageId) })
        }
    }
}
