package com.example.clean.entry.feature_auth.presentation.login

import androidx.compose.ui.window.ComposeUIViewController
import com.example.clean.entry.feature_auth.domain.model.Country
import platform.UIKit.UIViewController

fun LoginViewController(
    viewModel: LoginViewModel,
    onNavigateToCountryPicker: (Country) -> Unit,
    onLoginSuccess: () -> Unit,
    onCreateAccountClick: () -> Unit,
    countryResult: Country?,
    clearCountryResult: () -> Unit,
): UIViewController {
    return ComposeUIViewController {
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