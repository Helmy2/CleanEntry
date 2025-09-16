package com.example.clean.entry.feature.auth.presentation.registration

import androidx.compose.ui.window.ComposeUIViewController
import com.example.clean.entry.auth.domain.model.Country
import com.example.clean.entry.auth.presentation.registration.RegistrationRoute
import com.example.clean.entry.auth.presentation.registration.RegistrationViewModel
import com.example.clean.entry.core.design_system.CleanEntryTheme
import platform.UIKit.UIViewController


fun RegistrationViewController(
    viewModel: RegistrationViewModel,
    countryResult: Country?,
): UIViewController {
    return ComposeUIViewController {
        CleanEntryTheme {
            RegistrationRoute(
                viewModel = viewModel,
                countryResult = countryResult,
            )
        }
    }
}