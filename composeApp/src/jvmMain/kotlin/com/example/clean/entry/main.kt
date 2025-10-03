package com.example.clean.entry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.clean.entry.core.design_system.CleanEntryTheme
import com.example.clean.entry.core.util.PhoneNumberVerifier
import com.example.clean.entry.di.initKoin
import org.koin.compose.viewmodel.koinViewModel
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
            val viewModel = koinViewModel<MainViewModel>()
            val state by viewModel.startDestination.collectAsState()
            CleanEntryTheme {
                state?.let {
                    it.onSuccess { destination ->
                        App(destination)
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
}

val platformModule: Module = module {
    single { com.google.i18n.phonenumbers.PhoneNumberUtil.getInstance() }
    singleOf(::PhoneNumberVerifierImpl).bind<PhoneNumberVerifier>()
}