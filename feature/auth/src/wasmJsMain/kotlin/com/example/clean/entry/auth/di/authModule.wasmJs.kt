package com.example.clean.entry.auth.di

import com.example.clean.entry.auth.data.datastore.AuthDataStore
import com.example.clean.entry.auth.data.datastore.AuthDataStoreImpl
import com.example.clean.entry.auth.data.source.repository.CountryRepositoryImpl
import com.example.clean.entry.auth.domain.repository.CountryRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val authPlatformModule: Module = module {
    singleOf(::CountryRepositoryImpl).bind<CountryRepository>()
    singleOf(::AuthDataStoreImpl).bind<AuthDataStore>()
}