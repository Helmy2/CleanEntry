package com.example.clean.entry.feature_auth.di

import com.example.clean.entry.db.AppDatabase
import com.example.clean.entry.feature_auth.data.repository.CountryRepositoryImpl
import com.example.clean.entry.feature_auth.data.source.local.CountryLocalDataSource
import com.example.clean.entry.feature_auth.data.source.local.DatabaseDriverFactory
import com.example.clean.entry.feature_auth.domain.repository.CountryRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val nonJsModule: Module = module {
    singleOf(::CountryRepositoryImpl) {
        bind<CountryRepository>()
    }

    single<AppDatabase> {
        val driver = get<DatabaseDriverFactory>().createDriver()
        AppDatabase(driver)
    }

    factoryOf(::CountryLocalDataSource)
}
