package com.example.clean.entry.auth.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.clean.entry.auth.data.repository.CountryRepositoryImpl
import com.example.clean.entry.auth.domain.repository.CountryRepository
import com.example.clean.entry.db.AppDatabase
import com.example.clean.entry.feature.auth.data.source.local.CountryLocalDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val authPlatformModule: Module = module {
    single<AppDatabase> {
        AppDatabase(
            AndroidSqliteDriver(
                AppDatabase.Companion.Schema,
                androidContext(),
                "countries.db",
            )
        )
    }
    singleOf(::CountryRepositoryImpl) {
        bind<CountryRepository>()
    }

    factoryOf(::CountryLocalDataSource)
}