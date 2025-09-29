package com.example.clean.entry.shared.util

import com.example.clean.entry.auth.domain.repository.AuthRepository
import com.example.clean.entry.auth.presentation.country_code_picker.CountryCodePickerViewModel
import com.example.clean.entry.auth.presentation.login.LoginViewModel
import com.example.clean.entry.auth.presentation.profile.ProfileViewModel
import com.example.clean.entry.auth.presentation.registration.RegistrationViewModel
import com.example.clean.entry.core.navigation.AppNavigator
import com.example.clean.entry.core.util.PhoneNumberVerifier
import com.example.clean.entry.details.presentation.DetailsViewModel
import com.example.clean.entry.di.initKoin
import com.example.clean.entry.feed.presentation.FeedViewModel
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import platform.darwin.SInt64

class DependenciesHelper : KoinComponent {
    constructor(phoneNumberValidator: PhoneNumberVerifier) {
        initKoin(
            platformModule = module {
                factory<PhoneNumberVerifier> {
                    phoneNumberValidator
                }
            }
        )
    }

    val navigator: AppNavigator by inject()

    suspend fun isUserAuthenticated(): Boolean {
        val repository: AuthRepository by inject()
        return repository.isAuthenticated.first()
    }

    val loginViewModel: LoginViewModel by inject()
    val registrationViewModel: RegistrationViewModel by inject()
    fun countryCodePickerViewModel(code: String?): CountryCodePickerViewModel =
        getKoin().get { parametersOf(code) }


    val feedViewModel: FeedViewModel by inject()
    val profileViewModel: ProfileViewModel by inject()

    fun detailsViewModel(id: SInt64): DetailsViewModel =
        getKoin().get { parametersOf(id) }
}