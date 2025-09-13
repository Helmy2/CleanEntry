package com.example.clean.entry.feature.auth.presentation.login

import androidx.compose.ui.window.ComposeUIViewController
import com.example.clean.entry.core.design_system.CleanEntryTheme
import com.example.clean.entry.feature.auth.domain.model.Country
import platform.UIKit.UIViewController

fun LoginViewController(
    viewModel: LoginViewModel,
    countryResult: Country?,
): UIViewController {
    return ComposeUIViewController {
        CleanEntryTheme {
            LoginRoute(
                viewModel = viewModel,
                countryResult = countryResult,
            )
        }
    }
}