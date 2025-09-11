package com.example.clean.entry.feature_auth.di

import com.example.clean.entry.feature_auth.data.source.repository.CountryRepositoryImpl
import com.example.clean.entry.feature_auth.domain.repository.CountryRepository
import com.example.clean.entry.feature_auth.presentation.country_code_picker.CountryCodePickerViewModel
import com.example.clean.entry.feature_auth.util.PhoneNumberUtil
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

actual val authPlatformModule: Module = module {
    singleOf(::CountryRepositoryImpl) {
        bind<CountryRepository>()
    }
    singleOf(::PhoneNumberUtil)
}