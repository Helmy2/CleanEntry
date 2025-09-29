package com.example.clean.entry

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.example.clean.entry.core.util.PhoneNumberVerifier
import com.example.clean.entry.di.initKoin
import kotlinx.browser.document
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin(platformModule = platformModule)
    ComposeViewport(document.body!!) {
        App()
    }
}

val platformModule: Module = module {
    singleOf(::PhoneNumberVerifierImpl).bind<PhoneNumberVerifier>()
}