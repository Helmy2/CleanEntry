package com.example.clean.entry.feature.auth.presentation.country_code_picker

import androidx.compose.ui.window.ComposeUIViewController
import com.example.clean.entry.core.design_system.CleanEntryTheme
import com.example.clean.entry.feature.auth.domain.model.Country
import platform.UIKit.UIViewController


fun CountryCodePickerViewController(
    viewModel: CountryCodePickerViewModel,
    countryResult: Country?,
    onNavigateBack: (Country?) -> Unit,
): UIViewController {
    return ComposeUIViewController {
        CleanEntryTheme {
            CountryCodePickerRoute(
                viewModel = viewModel,
                countryResult = countryResult,
                onNavigateBack = onNavigateBack
            )
        }
    }
}
