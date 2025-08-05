package com.example.clean.entry.feature_auth.di

import com.apollographql.apollo.ApolloClient
import com.example.clean.entry.feature_auth.data.repository.CountryRepositoryImpl
import com.example.clean.entry.feature_auth.data.source.local.AppDatabase
import com.example.clean.entry.feature_auth.data.source.local.CountryLocalDataSource
import com.example.clean.entry.feature_auth.data.source.remote.CountryRemoteDataSource
import com.example.clean.entry.feature_auth.domain.repository.CountryRepository
import com.example.clean.entry.feature_auth.domain.usecase.ValidateEmailUseCase
import com.example.clean.entry.feature_auth.domain.usecase.ValidateFirstNameUseCase
import com.example.clean.entry.feature_auth.domain.usecase.ValidatePasswordUseCase
import com.example.clean.entry.feature_auth.domain.usecase.ValidatePhoneUseCase
import com.example.clean.entry.feature_auth.domain.usecase.ValidateSurnameUseCase
import com.example.clean.entry.feature_auth.presentation.country_code_picker.CountryCodePickerViewModel
import com.example.clean.entry.feature_auth.presentation.login.LoginViewModel
import com.example.clean.entry.feature_auth.presentation.registration.RegistrationViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val authPlatformModule: Module

/**
 * Koin module for the authentication feature.
 * This provides all the necessary dependencies for auth-related classes.
 */
val authModule = module {
    includes(authPlatformModule)

    factory { get<AppDatabase>().countryDao() }


    single {
        ApolloClient.Builder()
            .serverUrl("https://countries.trevorblades.com/graphql")
            .build()
    }

    factoryOf(::CountryLocalDataSource)
    factoryOf(::CountryRemoteDataSource)

    factoryOf(::CountryRepositoryImpl) { bind<CountryRepository>() }

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
