package com.example.clean.entry

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.clean.entry.core.design_system.CleanEntryTheme
import com.example.clean.entry.navigation.AppDestination
import com.example.clean.entry.navigation.AppNavHost

@Composable
fun App() {
    CleanEntryTheme {
        Scaffold {
            AppNavHost(
                modifier = Modifier.padding(it)
            )
        }
    }
}