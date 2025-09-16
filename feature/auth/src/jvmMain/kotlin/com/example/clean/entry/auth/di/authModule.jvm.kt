package com.example.clean.entry.auth.di

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.clean.entry.auth.data.repository.CountryRepositoryImpl
import com.example.clean.entry.auth.domain.repository.CountryRepository
import com.example.clean.entry.db.AppDatabase
import com.example.clean.entry.feature.auth.data.source.local.CountryLocalDataSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.util.Properties

actual val platformModule: Module = module {
    single<AppDatabase> {
        AppDatabase(JdbcSqliteDriver("jdbc:sqlite:countries.db", Properties(), AppDatabase.Schema))
    }
    singleOf(::CountryRepositoryImpl).bind<CountryRepository>()

    factoryOf(::CountryLocalDataSource)
}