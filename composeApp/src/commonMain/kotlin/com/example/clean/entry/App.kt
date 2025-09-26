package com.example.clean.entry

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.clean.entry.core.design_system.CleanEntryTheme
import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.navigation.AppNavHost

@Composable
fun App(
    startDestination: AppDestination = AppDestination.Auth
) {
    CleanEntryTheme {
        Scaffold {
            AppNavHost(
                startDestination,
                modifier = Modifier.padding(it)
            )
        }
    }
}