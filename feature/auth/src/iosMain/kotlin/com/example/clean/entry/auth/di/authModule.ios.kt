package com.example.clean.entry.auth.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.clean.entry.auth.data.datastore.AuthDataStore
import com.example.clean.entry.auth.data.datastore.AuthDataStoreImpl
import com.example.clean.entry.auth.data.datastore.createDataStore
import com.example.clean.entry.auth.data.datastore.dataStoreFileName
import com.example.clean.entry.auth.data.repository.CountryRepositoryImpl
import com.example.clean.entry.auth.data.source.local.CountryLocalDataSource
import com.example.clean.entry.auth.domain.repository.CountryRepository
import com.example.clean.entry.db.AppDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual val authPlatformModule: Module = module {
    single<AppDatabase> {
        AppDatabase(NativeSqliteDriver(AppDatabase.Companion.Schema, "countries.db"))
    }
    single {
        createDataStore(
            producePath = {
                val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )
                requireNotNull(documentDirectory).path + "/$dataStoreFileName"
            }
        )
    }

    factoryOf(::CountryRepositoryImpl).bind<CountryRepository>()

    factoryOf(::AuthDataStoreImpl).bind<AuthDataStore>()

    factoryOf(::CountryLocalDataSource)
}