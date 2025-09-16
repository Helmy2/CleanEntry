package com.example.clean.entry.feature.auth.di

import com.example.clean.entry.auth.domain.repository.CountryRepository
import com.example.clean.entry.feature.auth.data.repository.CountryRepositoryImpl
import com.example.clean.entry.feature.auth.data.source.local.CountryLocalDataSource
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Koin module for the authentication feature.
 * This provides all the necessary dependencies for auth-related classes.
 */
val nonJsAuthModule = module {

    singleOf(::CountryRepositoryImpl) {
        bind<CountryRepository>()
    }

    factoryOf(::CountryLocalDataSource)
}
