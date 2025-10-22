package com.example.clean.entry

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToBrowserNavigation
import com.example.clean.entry.core.components.SplashScreen
import com.example.clean.entry.core.design_system.CleanEntryTheme
import com.example.clean.entry.core.navigation.AppNavigator
import com.example.clean.entry.core.util.PhoneNumberVerifier
import com.example.clean.entry.di.initKoin
import kotlinx.browser.document
import org.koin.compose.koinInject
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalComposeUiApi::class, ExperimentalBrowserHistoryApi::class)
fun main() {
    initKoin(platformModule = platformModule)
    ComposeViewport(document.body!!) {
        val navigator: AppNavigator = koinInject()
        val initialDestination by navigator.initialDestination.collectAsState(null)
        CleanEntryTheme {
            AnimatedContent(
                targetState = initialDestination,
            ) { destination ->
                if (destination != null) {
                    App(
                        destination,
                        onNavHostReady = { it.bindToBrowserNavigation() }
                    )
                } else {
                    SplashScreen()
                }
            }
        }
    }
}

val platformModule: Module = module {
    singleOf(::PhoneNumberVerifierImpl).bind<PhoneNumberVerifier>()
}