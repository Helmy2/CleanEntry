package com.example.clean.entry.shared.di

import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.core.navigation.AppNavigator
import com.example.clean.entry.navigation.AppNavigatorImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { AppNavigatorImpl(AppDestination.Dashboard) }.bind<AppNavigator>()
}