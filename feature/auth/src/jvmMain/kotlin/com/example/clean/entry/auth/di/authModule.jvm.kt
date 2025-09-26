package com.example.clean.entry.auth.di

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.clean.entry.auth.data.datastore.AuthDataStore
import com.example.clean.entry.auth.data.datastore.AuthDataStoreImpl
import com.example.clean.entry.auth.data.datastore.createDataStore
import com.example.clean.entry.auth.data.datastore.dataStoreFileName
import com.example.clean.entry.auth.data.repository.CountryRepositoryImpl
import com.example.clean.entry.auth.data.source.local.CountryLocalDataSource
import com.example.clean.entry.auth.domain.repository.CountryRepository
import com.example.clean.entry.db.AppDatabase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.io.File
import java.util.Properties

actual val authPlatformModule: Module = module {
    single<AppDatabase> {
        AppDatabase(JdbcSqliteDriver("jdbc:sqlite:countries.db", Properties(), AppDatabase.Schema))
    }

    single {
        createDataStore(
            producePath = {
                val file = File(System.getProperty("java.io.tmpdir"), dataStoreFileName)
                file.absolutePath
            }
        )
    }

    factoryOf(::AuthDataStoreImpl).bind<AuthDataStore>()

    factoryOf(::CountryRepositoryImpl).bind<CountryRepository>()

    factoryOf(::CountryLocalDataSource)
}