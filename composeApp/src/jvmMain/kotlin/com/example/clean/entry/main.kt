package com.example.clean.entry

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.clean.entry.feature_auth.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Clean Entry",
        ) {
            App()
        }
    }
}