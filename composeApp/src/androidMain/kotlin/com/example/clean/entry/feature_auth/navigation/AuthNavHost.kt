package com.example.clean.entry.feature_auth.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clean.entry.feature_auth.domain.model.CountryData
import com.example.clean.entry.feature_auth.domain.model.toCountry
import com.example.clean.entry.feature_auth.presentation.country_code_picker.CountryCodePickerRoute
import com.example.clean.entry.feature_auth.presentation.login.LoginRoute
import com.example.clean.entry.feature_auth.presentation.registration.RegistrationRoute

private const val COUNTRY_RESULT_KEY = "country_result"


@Composable
fun AuthNavHost(modifier: Modifier = Modifier, onSuccess: () -> Unit) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthDestination.Login,
        modifier = modifier,
    ) {
        composable<AuthDestination.Login> { backStackEntry ->
            val countryResult = backStackEntry.savedStateHandle.get<CountryData>(COUNTRY_RESULT_KEY)
            LoginRoute(
                onCreateAccountClick = { navController.navigate(AuthDestination.Registration) },
                onNavigateToCountryPicker = {
                    navController.navigate(AuthDestination.CountryCodePicker(it.code))
                },
                onLoginSuccess = onSuccess,
                countryResult = countryResult?.toCountry(),
                clearCountryResult = {
                    backStackEntry.savedStateHandle.remove<CountryData>(
                        COUNTRY_RESULT_KEY
                    )
                },
            )
        }

        composable<AuthDestination.Registration> { backStackEntry ->
            val countryResult = backStackEntry.savedStateHandle.get<CountryData>(COUNTRY_RESULT_KEY)
            RegistrationRoute(countryResult = countryResult?.toCountry(), clearCountryResult = {
                backStackEntry.savedStateHandle.remove<CountryData>(
                    COUNTRY_RESULT_KEY
                )
            }, onNavigateToCountryPicker = {
                navController.navigate(AuthDestination.CountryCodePicker(it.code))
            }, onRegistrationSuccess = onSuccess, onBackClick = { navController.popBackStack() })
        }

        composable<AuthDestination.CountryCodePicker> {
            CountryCodePickerRoute(
                onNavigateBack = { navController.popBackStack() },
                setResult = { result ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        COUNTRY_RESULT_KEY, result
                    )
                },
            )
        }
    }
}