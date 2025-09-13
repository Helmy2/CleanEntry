package com.example.clean.entry.shared.di

import com.example.clean.entry.navigation.AppDestination
import com.example.clean.entry.navigation.AppNavigator
import org.koin.dsl.module

val appModule  = module {
    single { AppNavigator(AppDestination.Auth) }
}