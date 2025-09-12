package com.example.clean.entry.shared.di

import com.example.clean.entry.shared.data.source.local.DatabaseDriverFactory
import com.example.clean.entry.feature.auth.presentation.country_code_picker.CountryCodePickerViewModel
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

actual val platformModule: Module = module{
    includes(nonJsModule)
    singleOf(::DatabaseDriverFactory)

    single { PhoneNumberUtil.createInstance(androidContext()) }
    single { com.example.clean.entry.shared.util.PhoneNumberUtil(get()) }

    viewModelOf(::CountryCodePickerViewModel)
}
