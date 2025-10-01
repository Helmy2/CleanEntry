package com.example.clean.entry

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.clean.entry.core.util.PhoneNumberVerifier
import com.example.clean.entry.di.initKoin
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
            App()
        }
    }
}

val platformModule: Module = module {
    single { com.google.i18n.phonenumbers.PhoneNumberUtil.getInstance() }
    singleOf(::PhoneNumberVerifierImpl).bind<PhoneNumberVerifier>()
}