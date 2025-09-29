package com.example.clean.entry.di

import com.example.clean.entry.MainViewModel
import com.example.clean.entry.core.navigation.AppNavigator
import com.example.clean.entry.navigation.AppNavigatorImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single {
        AppNavigatorImpl(
            CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        )
    }.bind<AppNavigator>()

    viewModelOf(::MainViewModel)
}