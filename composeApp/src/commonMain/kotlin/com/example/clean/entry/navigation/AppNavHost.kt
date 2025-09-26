package com.example.clean.entry.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clean.entry.auth.navigation.authNavBuilder
import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.core.navigation.AppNavigator
import com.example.clean.entry.core.navigation.Command
import com.example.clean.entry.feed.presentation.FeedRoute
import org.koin.compose.koinInject


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

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        authNavBuilder()

        composable<AppDestination.Dashboard> {
            FeedRoute()
        }
    }
}
