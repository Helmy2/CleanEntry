package com.example.clean.entry.shared.di

import com.example.clean.entry.db.AppDatabase
import com.example.clean.entry.feature.auth.di.nonJsAuthModule
import com.example.clean.entry.shared.data.source.local.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

val nonJsModule: Module = module {
    single<AppDatabase> {
        val driver = get<DatabaseDriverFactory>().createDriver()
        AppDatabase(driver)
    }

    includes(nonJsAuthModule)
}
