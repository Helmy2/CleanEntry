package com.example.clean.entry.feature_auth.util

import com.example.clean.entry.feature_auth.domain.repository.CountryRepository
import com.example.clean.entry.feature_auth.presentation.login.LoginViewModel
import com.example.clean.entry.feature_auth.presentation.registration.RegistrationViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DependenciesHelper : KoinComponent {
    val loginViewModel: LoginViewModel by inject()
    val registrationViewModel: RegistrationViewModel by inject()
    val countryRepository: CountryRepository by inject()
}