package com.example.clean.entry.feature_auth.di

import com.example.clean.entry.feature_auth.domain.usecase.ValidateEmailUseCase
import com.example.clean.entry.feature_auth.domain.usecase.ValidateFirstNameUseCase
import com.example.clean.entry.feature_auth.domain.usecase.ValidatePasswordUseCase
import com.example.clean.entry.feature_auth.domain.usecase.ValidatePhoneUseCase
import com.example.clean.entry.feature_auth.domain.usecase.ValidateSurnameUseCase
import com.example.clean.entry.feature_auth.presentation.country_code_picker.CountryCodePickerViewModel
import com.example.clean.entry.feature_auth.presentation.login.LoginViewModel
import com.example.clean.entry.feature_auth.presentation.registration.RegistrationViewModel
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for the authentication feature.
 * This provides all the necessary dependencies for auth-related classes.
 */
val authModule = module {
    single { PhoneNumberUtil.createInstance(androidContext()) }

    factory{ ValidateEmailUseCase() }
    factoryOf(::ValidateFirstNameUseCase)
    factoryOf(::ValidateSurnameUseCase)
    factoryOf(::ValidatePhoneUseCase)
    factoryOf(::ValidatePasswordUseCase)

    viewModelOf(::RegistrationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::CountryCodePickerViewModel)
}
