package com.example.clean.entry

import android.app.Application
import com.example.clean.entry.core.util.PhoneNumberVerifier
import com.example.clean.entry.di.initKoin
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(platformModule = platformModule) {
            androidContext(this@MainApp)
        }
    }
}


val platformModule: Module = module {
    single { PhoneNumberUtil.createInstance(androidContext()) }
    single<PhoneNumberVerifier> { PhoneNumberVerifierImpl(get()) }
}