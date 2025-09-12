package com.example.clean.entry.feature_auth.di

import com.example.clean.entry.feature_auth.data.repository.CountryRepositoryImpl
import com.example.clean.entry.feature_auth.data.source.local.CountryLocalDataSource
import com.example.clean.entry.feature_auth.domain.repository.CountryRepository
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
