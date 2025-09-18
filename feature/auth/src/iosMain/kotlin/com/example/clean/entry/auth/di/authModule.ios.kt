package com.example.clean.entry.auth.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.clean.entry.auth.data.repository.CountryRepositoryImpl
import com.example.clean.entry.auth.domain.repository.CountryRepository
import com.example.clean.entry.db.AppDatabase
import com.example.clean.entry.feature.auth.data.source.local.CountryLocalDataSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val authPlatformModule: Module = module {
    single<AppDatabase> {
        AppDatabase(NativeSqliteDriver(AppDatabase.Companion.Schema, "countries.db"))
    }

    singleOf(::CountryRepositoryImpl) {
        bind<CountryRepository>()
    }

    factoryOf(::CountryLocalDataSource)
}