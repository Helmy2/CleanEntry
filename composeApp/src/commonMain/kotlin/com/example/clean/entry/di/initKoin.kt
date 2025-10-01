package com.example.clean.entry.di

import com.example.clean.entry.auth.di.authModule
import com.example.clean.entry.core.di.coreModule
import com.example.clean.entry.core.util.KoinNapierLogger
import com.example.clean.entry.shared.di.homeModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(platformModule: Module = Module(), config: KoinAppDeclaration? = null) {
    Napier.base(DebugAntilog())

    startKoin {
        logger(KoinNapierLogger)
        config?.invoke(this)
        modules(platformModule, coreModule, appModule, authModule, homeModule)
    }
}