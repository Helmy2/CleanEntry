package com.example.clean.entry.feature_auth.di

import com.example.clean.entry.feature_auth.data.source.local.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val authPlatformModule: Module = module {
    includes(nonJsModule)
    singleOf(::DatabaseDriverFactory)

    single { com.example.clean.entry.feature_auth.util.PhoneNumberUtil() }
}