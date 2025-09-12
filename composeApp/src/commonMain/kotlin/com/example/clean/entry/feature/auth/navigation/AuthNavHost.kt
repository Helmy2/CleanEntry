package com.example.clean.entry.feature.auth.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clean.entry.feature.auth.domain.model.Country
import com.example.clean.entry.feature.auth.presentation.country_code_picker.CountryCodePickerRoute
import com.example.clean.entry.feature.auth.presentation.login.LoginRoute
import com.example.clean.entry.feature.auth.presentation.registration.RegistrationRoute


@Composable
fun AuthNavHost(modifier: Modifier = Modifier, onSuccess: () -> Unit) {
    val navController = rememberNavController()
    var country by remember {
        mutableStateOf(
            Country(
                name = "Egypt", dialCode = "+20", code = "EG", flagEmoji = "🇪🇬"
            )
        )
    }

    NavHost(
        navController = navController,
        startDestination = AuthDestination.Login,
        modifier = modifier,
    ) {
        composable<AuthDestination.Login> { backStackEntry ->
            LoginRoute(
                onCreateAccountClick = { navController.navigate(AuthDestination.Registration) },
                onNavigateToCountryPicker = {
                    navController.navigate(AuthDestination.CountryCodePicker(it.code))
                },
                onLoginSuccess = onSuccess,
                countryResult = country,
            )
        }

        composable<AuthDestination.Registration> { backStackEntry ->
            RegistrationRoute(
                countryResult = country,
                onNavigateToCountryPicker = {
                    navController.navigate(AuthDestination.CountryCodePicker(it.code))
                },
                onRegistrationSuccess = onSuccess, onBackClick = { navController.popBackStack() },
            )
        }

        composable<AuthDestination.CountryCodePicker> {
            CountryCodePickerRoute(
                countryResult = country,
                onNavigateBack = {
                    it?.let {
                        country = it
                    }
                    navController.popBackStack()
                },
            )
        }
    }
}