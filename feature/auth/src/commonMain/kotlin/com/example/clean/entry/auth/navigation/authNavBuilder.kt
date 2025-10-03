package com.example.clean.entry.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.clean.entry.auth.presentation.country_code_picker.CountryCodePickerRoute
import com.example.clean.entry.auth.presentation.login.LoginRoute
import com.example.clean.entry.auth.presentation.profile.ProfileRoute
import com.example.clean.entry.auth.presentation.registration.RegistrationRoute
import com.example.clean.entry.core.navigation.AppDestination

fun NavGraphBuilder.authNavBuilder() {
    composable<AppDestination.Login> { backStackEntry ->
        LoginRoute(
            countryResult = null,
        )
    }

    composable<AppDestination.Registration> { backStackEntry ->
        RegistrationRoute(
            countryResult = null,
        )
    }

    composable<AppDestination.CountryCodePicker> { backStackEntry ->
        val country = backStackEntry.toRoute<AppDestination.CountryCodePicker>().code
        CountryCodePickerRoute(
            countryCode = country,
        )
    }

    composable<AppDestination.Profile> {
        ProfileRoute()
    }
}

