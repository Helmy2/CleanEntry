package com.example.clean.entry.di

import com.example.clean.entry.auth.di.authModule
import com.example.clean.entry.shared.di.homeModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(platformModule: Module = Module(), config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(platformModule, appModule, authModule, homeModule)
    }
}