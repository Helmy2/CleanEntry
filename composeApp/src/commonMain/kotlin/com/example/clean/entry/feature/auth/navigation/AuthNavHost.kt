package com.example.clean.entry.feature.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.clean.entry.feature.auth.domain.model.Country
import com.example.clean.entry.feature.auth.presentation.country_code_picker.CountryCodePickerRoute
import com.example.clean.entry.feature.auth.presentation.login.LoginRoute
import com.example.clean.entry.feature.auth.presentation.registration.RegistrationRoute
import com.example.clean.entry.navigation.AppDestination
import com.example.clean.entry.navigation.AppNavigator

fun NavGraphBuilder.authNavBuilder(
    country: Country,
    onCountryChange: (Country) -> Unit,
    navigator: AppNavigator,
) {
    navigation<AppDestination.Auth>(
        startDestination = AppDestination.Auth.Login
    ) {

        composable<AppDestination.Auth.Login> { backStackEntry ->
            LoginRoute(
                onCreateAccountClick = {
                    navigator.navigate(AppDestination.Auth.Registration)
                },
                onNavigateToCountryPicker = {
                    navigator.navigate(AppDestination.Auth.CountryCodePicker(it.code))
                },
                onLoginSuccess = {
                    navigator.navigateAsRoot(AppDestination.Dashboard)
                },
                countryResult = country,
            )
        }

        composable<AppDestination.Auth.Registration> { backStackEntry ->
            RegistrationRoute(
                countryResult = country,
                onNavigateToCountryPicker = {
                    navigator.navigate(AppDestination.Auth.CountryCodePicker(it.code))
                },
                onRegistrationSuccess = {
                    navigator.navigateAsRoot(AppDestination.Dashboard)
                },
                onBackClick = { navigator.navigateBack() },
            )
        }

        composable<AppDestination.Auth.CountryCodePicker> {
            CountryCodePickerRoute(
                countryResult = country,
                onNavigateBack = {
                    it?.let {
                        onCountryChange(it)
                    }
                    navigator.navigateBack()
                },
            )
        }
    }
}