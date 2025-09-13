package com.example.clean.entry.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clean.entry.feature.auth.navigation.authNavBuilder
import org.koin.compose.koinInject


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val navigator = koinInject<AppNavigator>()

    LaunchedEffect(navigator.commands.value) {
        navigator.commands.collect { command ->
            when (command) {
                is Command.NavigateAsRoot -> {
                    navController.navigate(command.destination) {
                        popUpTo(0)
                    }
                }

                Command.NavigateBack -> navController.popBackStack()
                is Command.NavigateTo -> navController.navigate(command.destination)
                is Command.NavigateBackWithResult -> {
                    navigator.setValue(command.key, command.value)
                    navController.popBackStack()
                }

                Command.Idle -> {
                    // Do nothing
                }
            }
            navigator.onCommandConsumed()
        }
    }

    NavHost(
        navController = navController,
        startDestination = navigator.startDestination,
        modifier = modifier,
    ) {
        authNavBuilder()

        composable<AppDestination.Dashboard> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Welcome! You are logged in.")
            }
        }
    }
}
