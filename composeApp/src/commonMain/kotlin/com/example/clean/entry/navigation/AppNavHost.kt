package com.example.clean.entry.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.clean.entry.feature.auth.domain.model.Country
import com.example.clean.entry.feature.auth.navigation.authNavBuilder
import com.example.clean.entry.feature.auth.presentation.country_code_picker.CountryCodePickerRoute
import com.example.clean.entry.feature.auth.presentation.login.LoginRoute
import com.example.clean.entry.feature.auth.presentation.registration.RegistrationRoute
import org.koin.compose.koinInject


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val navigator = koinInject<AppNavigator>()
    var country by remember {
        mutableStateOf(
            Country(
                name = "Egypt", dialCode = "+20", code = "EG", flagEmoji = "🇪🇬"
            )
        )
    }

    LaunchedEffect(navigator.commands.value) {
        navigator.commands.value?.let { command ->
            when (command) {
                is Command.NavigateAsRoot -> {
                    navController.navigate(command.destination) {
                        popUpTo(0)
                    }
                }

                Command.NavigateBack -> navController.popBackStack()
                is Command.NavigateTo -> navController.navigate(command.destination)
            }
            navigator.onCommandConsumed()
        }
    }

    NavHost(
        navController = navController,
        startDestination = navigator.startDestination,
        modifier = modifier,
    ) {
        authNavBuilder(
            country = country,
            navigator = navigator,
            onCountryChange = { country = it }
        )

        composable<AppDestination.Dashboard> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Welcome! You are logged in.")
            }
        }
    }
}
