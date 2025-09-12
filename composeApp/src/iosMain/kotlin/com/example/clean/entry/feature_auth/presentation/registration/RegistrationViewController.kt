package com.example.clean.entry.feature_auth.presentation.registration

import androidx.compose.ui.window.ComposeUIViewController
import com.example.clean.entry.core.design_system.CleanEntryTheme
import com.example.clean.entry.feature_auth.domain.model.Country
import platform.UIKit.UIViewController


fun RegistrationViewController(
    viewModel: RegistrationViewModel,
    onBackClick: () -> Unit,
    countryResult: Country?,
    onNavigateToCountryPicker: (Country) -> Unit,
    onRegistrationSuccess: () -> Unit
): UIViewController {
    return ComposeUIViewController {
        CleanEntryTheme {
            RegistrationRoute(
                viewModel = viewModel,
                onNavigateToCountryPicker = onNavigateToCountryPicker,
                onBackClick = onBackClick,
                onRegistrationSuccess = onRegistrationSuccess,
                countryResult = countryResult,
            )
        }
    }
}