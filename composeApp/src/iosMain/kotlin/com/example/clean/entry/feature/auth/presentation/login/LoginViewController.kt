package com.example.clean.entry.feature.auth.presentation.login

import androidx.compose.ui.window.ComposeUIViewController
import com.example.clean.entry.auth.domain.model.Country
import com.example.clean.entry.auth.presentation.login.LoginRoute
import com.example.clean.entry.auth.presentation.login.LoginViewModel
import com.example.clean.entry.core.design_system.CleanEntryTheme
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