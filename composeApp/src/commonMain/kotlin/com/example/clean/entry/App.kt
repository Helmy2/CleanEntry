package com.example.clean.entry

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.navigation.AppNavHost

@Composable
fun App(
    startDestination: AppDestination,
    onNavHostReady: suspend (NavController) -> Unit = {},
) {
    AppNavHost(
        startDestination,
        onNavHostReady = onNavHostReady
    )
}