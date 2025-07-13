package com.example.clean.entry.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.presentation.country_code_picker.CountryCodePickerRoute
import com.example.clean.entry.feature_auth.presentation.login.LoginRoute
import com.example.clean.entry.feature_auth.presentation.registration.RegistrationRoute


private const val COUNTRY_RESULT_KEY = "country_result"

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: Any
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        navigation<Destination.AuthGraph>(
            startDestination = Destination.Login
        ) {
            composable<Destination.Login> { backStackEntry ->
                val countryResult = backStackEntry.savedStateHandle.get<Country>(COUNTRY_RESULT_KEY)
                LoginRoute(
                    onCreateAccountClick = { navController.navigate(Destination.Registration) },
                    onNavigateToCountryPicker = {
                        navController.navigate(Destination.CountryCodePicker(it.code))
                    },
                    onLoginSuccess = {
                        navController.navigate(Destination.MainGraph) {
                            popUpTo(Destination.AuthGraph) { inclusive = true }
                        }
                    },
                    countryResult = countryResult,
                    clearCountryResult = {
                        backStackEntry.savedStateHandle.remove<Country>(
                            COUNTRY_RESULT_KEY
                        )
                    },
                )
            }

            composable<Destination.Registration> { backStackEntry ->
                val countryResult = backStackEntry.savedStateHandle.get<Country>(COUNTRY_RESULT_KEY)
                RegistrationRoute(
                    countryResult = countryResult,
                    clearCountryResult = {
                        backStackEntry.savedStateHandle.remove<Country>(
                            COUNTRY_RESULT_KEY
                        )
                    },
                    onNavigateToCountryPicker = {
                        navController.navigate(Destination.CountryCodePicker(it.code))
                    },
                    onRegistrationSuccess = {
                        navController.navigate(Destination.MainGraph) {
                            popUpTo(Destination.AuthGraph) { inclusive = true }
                        }
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable<Destination.CountryCodePicker> {
                CountryCodePickerRoute(
                    onNavigateBack = { navController.popBackStack() },
                    setResult = { result ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(COUNTRY_RESULT_KEY, result)
                    },
                )
            }
        }

        navigation<Destination.MainGraph>(
            startDestination = Destination.Dashboard
        ) {
            composable<Destination.Dashboard> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Welcome! You are logged in.")
                }
            }
        }
    }
}
