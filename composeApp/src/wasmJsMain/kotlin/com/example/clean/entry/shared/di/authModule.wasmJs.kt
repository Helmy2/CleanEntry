package com.example.clean.entry.shared.di

import com.example.clean.entry.feature.auth.data.source.repository.CountryRepositoryImpl
import com.example.clean.entry.feature.auth.domain.repository.CountryRepository
import com.example.clean.entry.shared.util.PhoneNumberUtil
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule: Module = module {
    singleOf(::CountryRepositoryImpl) {
        bind<CountryRepository>()
    }
    singleOf(::PhoneNumberUtil)
}