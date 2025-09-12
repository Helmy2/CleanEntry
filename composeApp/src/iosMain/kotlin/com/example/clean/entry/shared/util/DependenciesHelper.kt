package com.example.clean.entry.shared.util

import com.example.clean.entry.auth.presentation.country_code_picker.CountryCodePickerViewModel
import com.example.clean.entry.auth.presentation.login.LoginViewModel
import com.example.clean.entry.auth.presentation.registration.RegistrationViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DependenciesHelper : KoinComponent {
    val loginViewModel: LoginViewModel by inject()
    val registrationViewModel: RegistrationViewModel by inject()
    val countryCodePickerViewModel: CountryCodePickerViewModel by inject()
}