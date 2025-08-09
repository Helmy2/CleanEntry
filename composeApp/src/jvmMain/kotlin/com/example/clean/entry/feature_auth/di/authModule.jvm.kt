package com.example.clean.entry.feature_auth.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.clean.entry.feature_auth.data.source.local.AppDatabase
import com.example.clean.entry.feature_auth.presentation.country_code_picker.CountryCodePickerViewModel
import com.example.clean.entry.feature_auth.util.PhoneNumberUtil
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import java.io.File

actual val authPlatformModule: Module = module {
    single {
        val dbFile = File(System.getProperty("java.io.tmpdir"), "countries.db")
        Room.databaseBuilder<AppDatabase>(
            dbFile.absolutePath,
        ).fallbackToDestructiveMigration(true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    single { PhoneNumberUtil() }
    viewModelOf(::CountryCodePickerViewModel)
}