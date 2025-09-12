package com.example.clean.entry.shared.di

import com.example.clean.entry.shared.data.source.local.DatabaseDriverFactory
import com.example.clean.entry.shared.util.PhoneNumberUtil
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


actual val platformModule: Module = module {
    includes(nonJsModule)
    singleOf(::DatabaseDriverFactory)

    single { PhoneNumberUtil() }
}