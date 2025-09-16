package com.example.clean.entry.auth.di

import com.apollographql.apollo.ApolloClient
import com.example.clean.entry.auth.data.source.remote.CountryRemoteDataSource
import com.example.clean.entry.auth.domain.usecase.ValidateEmailUseCase
import com.example.clean.entry.auth.domain.usecase.ValidateFirstNameUseCase
import com.example.clean.entry.auth.domain.usecase.ValidatePasswordUseCase
import com.example.clean.entry.auth.domain.usecase.ValidatePhoneUseCase
import com.example.clean.entry.auth.domain.usecase.ValidateSurnameUseCase
import com.example.clean.entry.auth.presentation.country_code_picker.CountryCodePickerViewModel
import com.example.clean.entry.auth.presentation.login.LoginViewModel
import com.example.clean.entry.auth.presentation.registration.RegistrationViewModel
import com.example.clean.entry.db.AppDatabase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for the authentication feature.
 * This provides all the necessary dependencies for auth-related classes.
 */
val authModule = module {

    factory { get<AppDatabase>().countryEntityQueries }

    single {
        ApolloClient.Builder()
            .serverUrl("https://countries.trevorblades.com/graphql")
            .build()
    }

    factoryOf(::CountryRemoteDataSource)

    // --- DOMAIN LAYER ---
    factoryOf(::ValidateEmailUseCase)
    factoryOf(::ValidateFirstNameUseCase)
    factoryOf(::ValidateSurnameUseCase)
    factoryOf(::ValidatePhoneUseCase)
    factoryOf(::ValidatePasswordUseCase)

    // --- PRESENTATION LAYER ---
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::CountryCodePickerViewModel)
}
