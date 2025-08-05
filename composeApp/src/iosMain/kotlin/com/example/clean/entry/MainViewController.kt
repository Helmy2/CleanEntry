package com.example.clean.entry

import androidx.compose.material3.Scaffold
import androidx.compose.ui.window.ComposeUIViewController
import com.example.clean.entry.core.design_system.CleanEntryTheme
import com.example.clean.entry.feature_auth.di.initKoin
import com.example.clean.entry.feature_auth.presentation.login.LoginRoute
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    return ComposeUIViewController(
        configure = { initKoin() },
    ) {
        CleanEntryTheme {
            Scaffold(
            ) {
                LoginRoute(
                    onCreateAccountClick = { },
                    onNavigateToCountryPicker = {},
                    onLoginSuccess = {},
                    countryResult = null,
                    clearCountryResult = {},
                )
            }
        }
    }
}