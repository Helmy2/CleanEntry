package com.example.clean.entry.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.clean.entry.feature.auth.navigation.AuthNavHost


@Composable
fun AppNavHost(
    navController: NavHostController, modifier: Modifier = Modifier, startDestination: Any
) {
    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {
        composable<AppDestination.AuthGraph> {
            AuthNavHost(
                onSuccess = {
                    navController.navigate(AppDestination.MainGraph) {
                        popUpTo(AppDestination.AuthGraph) { inclusive = true }
                    }
                },
            )
        }

        navigation<AppDestination.MainGraph>(
            startDestination = AppDestination.Dashboard
        ) {
            composable<AppDestination.Dashboard> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Welcome! You are logged in.")
                }
            }
        }
    }
}
