package com.example.clean.entry.shared.di

import com.example.clean.entry.auth.presentation.country_code_picker.CountryCodePickerViewModel
import com.example.clean.entry.core.util.PhoneNumberVerifier
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { PhoneNumberUtil.createInstance(androidContext()) }
    single { com.example.clean.entry.shared.util.PhoneNumberVerifierImpl(get()) }
        .bind<PhoneNumberVerifier>()

    viewModelOf(::CountryCodePickerViewModel)
}
