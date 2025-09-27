package com.example.clean.entry.shared.util

import com.example.clean.entry.auth.domain.repository.AuthRepository
import com.example.clean.entry.auth.presentation.country_code_picker.CountryCodePickerViewModel
import com.example.clean.entry.auth.presentation.login.LoginViewModel
import com.example.clean.entry.auth.presentation.profile.ProfileViewModel
import com.example.clean.entry.auth.presentation.registration.RegistrationViewModel
import com.example.clean.entry.core.navigation.AppNavigator
import com.example.clean.entry.feed.presentation.FeedViewModel
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DependenciesHelper : KoinComponent {
    val loginViewModel: LoginViewModel by inject()
    val registrationViewModel: RegistrationViewModel by inject()
    val countryCodePickerViewModel: CountryCodePickerViewModel by inject()
    val feedViewModel: FeedViewModel by inject()
    val profileViewModel: ProfileViewModel by inject()
    val navigator: AppNavigator by inject()

    suspend fun isUserAuthenticated(): Boolean {
        val repository: AuthRepository by inject()
        return repository.isAuthenticated.first()
    }
}