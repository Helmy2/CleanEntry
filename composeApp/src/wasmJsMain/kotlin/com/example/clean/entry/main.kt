package com.example.clean.entry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToBrowserNavigation
import com.example.clean.entry.core.design_system.CleanEntryTheme
import com.example.clean.entry.core.util.PhoneNumberVerifier
import com.example.clean.entry.di.initKoin
import kotlinx.browser.document
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalComposeUiApi::class, ExperimentalBrowserHistoryApi::class)
fun main() {
    initKoin(platformModule = platformModule)
    ComposeViewport(document.body!!) {
        val viewModel = koinViewModel<MainViewModel>()
        val state by viewModel.startDestination.collectAsState()
        CleanEntryTheme {
            state?.let {
                it.onSuccess { destination ->
                    App(
                        startDestination = destination,
                        onNavHostReady = { it.bindToBrowserNavigation() }
                    )
                }.onFailure {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("Something went wrong. Please try again later.")
                    }
                }
            }
        }
    }
}

val platformModule: Module = module {
    singleOf(::PhoneNumberVerifierImpl).bind<PhoneNumberVerifier>()
}