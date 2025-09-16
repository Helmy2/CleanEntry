package com.example.clean.entry.shared.di

import com.example.clean.entry.auth.presentation.country_code_picker.CountryCodePickerViewModel
import com.example.clean.entry.core.util.PhoneNumberVerifier
import com.example.clean.entry.shared.util.PhoneNumberVerifierImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { com.google.i18n.phonenumbers.PhoneNumberUtil.getInstance() }
    singleOf(::PhoneNumberVerifierImpl).bind<PhoneNumberVerifier>()
    viewModelOf(::CountryCodePickerViewModel)
}