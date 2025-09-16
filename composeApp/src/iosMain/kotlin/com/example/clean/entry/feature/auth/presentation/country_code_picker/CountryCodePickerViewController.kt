package com.example.clean.entry.feature.auth.presentation.country_code_picker

import androidx.compose.ui.window.ComposeUIViewController
import com.example.clean.entry.auth.domain.model.Country
import com.example.clean.entry.auth.presentation.country_code_picker.CountryCodePickerRoute
import com.example.clean.entry.auth.presentation.country_code_picker.CountryCodePickerViewModel
import com.example.clean.entry.core.design_system.CleanEntryTheme
import platform.UIKit.UIViewController


fun CountryCodePickerViewController(
    viewModel: CountryCodePickerViewModel,
    countryResult: Country?,
): UIViewController {
    return ComposeUIViewController {
        CleanEntryTheme {
            CountryCodePickerRoute(
                viewModel = viewModel,
                countryCode = countryResult?.code,
            )
        }
    }
}
