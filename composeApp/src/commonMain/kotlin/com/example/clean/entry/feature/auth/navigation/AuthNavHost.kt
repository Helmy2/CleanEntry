package com.example.clean.entry.feature.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.clean.entry.feature.auth.presentation.country_code_picker.CountryCodePickerRoute
import com.example.clean.entry.feature.auth.presentation.login.LoginRoute
import com.example.clean.entry.feature.auth.presentation.registration.RegistrationRoute
import com.example.clean.entry.navigation.AppDestination

fun NavGraphBuilder.authNavBuilder() {
    navigation<AppDestination.Auth>(
        startDestination = AppDestination.Auth.Login
    ) {

        composable<AppDestination.Auth.Login> { backStackEntry ->
            LoginRoute(
                countryResult = null,
            )
        }

        composable<AppDestination.Auth.Registration> { backStackEntry ->
            RegistrationRoute(
                countryResult = null,
            )
        }

        composable<AppDestination.Auth.CountryCodePicker>(
        ) { backStackEntry ->
            val country = backStackEntry.toRoute<AppDestination.Auth.CountryCodePicker>().code
            CountryCodePickerRoute(
                countryCode = country,
            )
        }
    }
}