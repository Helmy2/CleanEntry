package com.example.clean.entry.shared.di

import com.example.clean.entry.core.navigation.AppNavigator
import com.example.clean.entry.navigation.AppNavigatorImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single {
        AppNavigatorImpl(
            CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        )
    }.bind<AppNavigator>()
}