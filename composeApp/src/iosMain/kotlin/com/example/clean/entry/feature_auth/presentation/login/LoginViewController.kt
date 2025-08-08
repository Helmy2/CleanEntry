package com.example.clean.entry.feature_auth.presentation.login

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.ComposeUIViewController
import com.example.clean.entry.core.design_system.CleanEntryTheme
import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.util.NativeViewFactory
import platform.UIKit.UIViewController

val LocalNativeViewFactory = staticCompositionLocalOf<NativeViewFactory> {
    error("No view factory provided.")
}

fun LoginViewController(
    nativeViewFactory: NativeViewFactory,
    viewModel: LoginViewModel,
    onNavigateToCountryPicker: (Country) -> Unit,
    onLoginSuccess: () -> Unit,
    onCreateAccountClick: () -> Unit,
    countryResult: Country?,
    clearCountryResult: () -> Unit,
): UIViewController {
    return ComposeUIViewController {
        CleanEntryTheme {
            CompositionLocalProvider(LocalNativeViewFactory provides nativeViewFactory) {
                LoginRoute(
                    viewModel = viewModel,
                    onNavigateToCountryPicker = onNavigateToCountryPicker,
                    onLoginSuccess = onLoginSuccess,
                    onCreateAccountClick = onCreateAccountClick,
                    countryResult = countryResult,
                    clearCountryResult = clearCountryResult
                )
            }
        }
    }
}