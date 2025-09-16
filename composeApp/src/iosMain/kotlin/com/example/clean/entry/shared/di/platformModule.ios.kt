package com.example.clean.entry.shared.di

import com.example.clean.entry.core.util.PhoneNumberVerifier
import com.example.clean.entry.shared.util.PhoneNumberVerifierImpl
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module


actual val platformModule: Module = module {
    single { PhoneNumberVerifierImpl() }.bind<PhoneNumberVerifier>()
}