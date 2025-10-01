package com.example.clean.entry.auth.di

import com.apollographql.apollo.ApolloClient
import com.example.clean.entry.auth.data.repository.AuthRepositoryImpl
import com.example.clean.entry.auth.data.source.remote.AuthRemoteDataSource
import com.example.clean.entry.auth.data.source.remote.CountryRemoteDataSource
import com.example.clean.entry.auth.domain.repository.AuthRepository
import com.example.clean.entry.auth.domain.usecase.ValidateConfirmPasswordUseCase
import com.example.clean.entry.auth.domain.usecase.ValidateEmailUseCase
import com.example.clean.entry.auth.domain.usecase.ValidatePasswordUseCase
import com.example.clean.entry.auth.domain.usecase.ValidatePhoneUseCase
import com.example.clean.entry.auth.presentation.country_code_picker.CountryCodePickerViewModel
import com.example.clean.entry.auth.presentation.login.LoginViewModel
import com.example.clean.entry.auth.presentation.profile.ProfileViewModel
import com.example.clean.entry.auth.presentation.registration.RegistrationViewModel
import com.example.clean.entry.db.AppDatabase
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val authPlatformModule: Module

val authModule = module {
    includes(authPlatformModule)

    single { get<AppDatabase>().countryEntityQueries }

    single {
        ApolloClient.Builder()
            .serverUrl("https://countries.trevorblades.com/graphql")
            .build()
    }

    factoryOf(::CountryRemoteDataSource)
    factoryOf(::AuthRemoteDataSource)
    factoryOf(::AuthRepositoryImpl) { bind<AuthRepository>() }

    // --- DOMAIN LAYER ---
    factoryOf(::ValidateEmailUseCase)
    factoryOf(::ValidatePhoneUseCase)
    factoryOf(::ValidatePasswordUseCase)
    factoryOf(::ValidateConfirmPasswordUseCase)

    // --- PRESENTATION LAYER ---
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::CountryCodePickerViewModel)
    viewModelOf(::ProfileViewModel)
}
