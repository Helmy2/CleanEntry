package com.example.clean.entry.auth.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.clean.entry.auth.data.datastore.AuthDataStore
import com.example.clean.entry.auth.data.datastore.AuthDataStoreImpl
import com.example.clean.entry.auth.data.datastore.createDataStore
import com.example.clean.entry.auth.data.datastore.dataStoreFileName
import com.example.clean.entry.auth.data.repository.CountryRepositoryImpl
import com.example.clean.entry.auth.data.source.local.CountryLocalDataSource
import com.example.clean.entry.auth.domain.repository.CountryRepository
import com.example.clean.entry.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
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
    single {
        createDataStore(
            producePath = { androidContext().filesDir.resolve(dataStoreFileName).absolutePath }
        )
    }

    factoryOf(::AuthDataStoreImpl).bind<AuthDataStore>()

    factoryOf(::CountryRepositoryImpl).bind<CountryRepository>()

    factoryOf(::CountryLocalDataSource)
}