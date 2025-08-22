package com.example.clean.entry

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.compose.rememberNavController
import com.example.clean.entry.core.design_system.CleanEntryTheme
import com.example.clean.entry.feature_auth.di.initKoin
import com.example.clean.entry.navigation.AppDestination
import com.example.clean.entry.navigation.AppNavHost
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    ComposeViewport(document.body!!) {
        CleanEntryTheme {
            Scaffold {
                val navController = rememberNavController()
                AppNavHost(
                    navController = navController,
                    startDestination = AppDestination.AuthGraph,
                    modifier = Modifier.padding(it)
                )
            }
        }
    }
}