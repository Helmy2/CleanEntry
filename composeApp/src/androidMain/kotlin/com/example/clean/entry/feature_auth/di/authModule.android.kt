package com.example.clean.entry.feature_auth.di

import com.example.clean.entry.feature_auth.data.source.local.DatabaseDriverFactory
import com.example.clean.entry.feature_auth.presentation.country_code_picker.CountryCodePickerViewModel
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

actual val authPlatformModule: Module = module{
    singleOf(::DatabaseDriverFactory)

    single { PhoneNumberUtil.createInstance(androidContext()) }
    single { com.example.clean.entry.feature_auth.util.PhoneNumberUtil(get()) }

    viewModelOf(::CountryCodePickerViewModel)
}
