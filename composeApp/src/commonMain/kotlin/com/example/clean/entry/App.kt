package com.example.clean.entry

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.clean.entry.core.design_system.CleanEntryTheme
import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.navigation.AppNavHost

@Composable
fun App(
    startDestination: AppDestination = AppDestination.Auth,
    onNavHostReady: suspend (NavController) -> Unit = {},
) {
    CleanEntryTheme {
        AppNavHost(
            startDestination,
            onNavHostReady = onNavHostReady
        )
    }
}