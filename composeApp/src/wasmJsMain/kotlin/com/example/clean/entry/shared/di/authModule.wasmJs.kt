package com.example.clean.entry.shared.di

import com.example.clean.entry.auth.domain.repository.CountryRepository
import com.example.clean.entry.core.util.PhoneNumberVerifier
import com.example.clean.entry.feature.auth.data.source.repository.CountryRepositoryImpl
import com.example.clean.entry.shared.util.PhoneNumberVerifierImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    singleOf(::CountryRepositoryImpl).bind<CountryRepository>()
    singleOf(::PhoneNumberVerifierImpl).bind<PhoneNumberVerifier>()
}