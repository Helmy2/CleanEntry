package com.example.clean.entry.feature_auth.presentation.registration

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.example.clean.entry.core.design_system.CleanEntryTheme
import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.presentation.login.LocalNativeViewFactory
import com.example.clean.entry.feature_auth.util.NativeViewFactory
import platform.UIKit.UIViewController


fun RegistrationViewController(
    nativeViewFactory: NativeViewFactory,
    viewModel: RegistrationViewModel,
    onBackClick: () -> Unit,
    countryResult: Country?,
    clearCountryResult: () -> Unit,
    onNavigateToCountryPicker: (Country) -> Unit,
    onRegistrationSuccess: () -> Unit
): UIViewController {
    return ComposeUIViewController {
        CleanEntryTheme {
            CompositionLocalProvider(LocalNativeViewFactory provides nativeViewFactory) {
                RegistrationRoute(
                    viewModel = viewModel,
                    onNavigateToCountryPicker = onNavigateToCountryPicker,
                    onBackClick = onBackClick,
                    onRegistrationSuccess = onRegistrationSuccess,
                    countryResult = countryResult,
                    clearCountryResult = clearCountryResult
                )
            }
        }
    }
}