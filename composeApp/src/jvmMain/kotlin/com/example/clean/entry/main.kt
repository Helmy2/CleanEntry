package com.example.clean.entry

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.clean.entry.core.components.SplashScreen
import com.example.clean.entry.core.design_system.CleanEntryTheme
import com.example.clean.entry.core.navigation.AppNavigator
import com.example.clean.entry.core.util.PhoneNumberVerifier
import com.example.clean.entry.di.initKoin
import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.koin.compose.koinInject
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun main() {
    initKoin(platformModule = platformModule)
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Clean Entry",
        ) {
            val navigator: AppNavigator = koinInject()
            val initialDestination by navigator.initialDestination.collectAsState(null)
            CleanEntryTheme {
                AnimatedContent(
                    targetState = initialDestination,
                ) { destination ->
                    if (destination != null) {
                        App(destination)
                    } else {
                        SplashScreen()
                    }
                }
            }
        }
    }
}

val platformModule: Module = module {
    single { PhoneNumberUtil.getInstance() }
    singleOf(::PhoneNumberVerifierImpl).bind<PhoneNumberVerifier>()
}