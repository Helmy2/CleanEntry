package com.example.clean.entry.feature_auth.di

import androidx.room.Room
import com.example.clean.entry.feature_auth.data.source.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSHomeDirectory

actual val authPlatformModule: Module = module {
    single {
        val dbFile = NSHomeDirectory() + "/countries.db"
        Room.databaseBuilder<AppDatabase>(
            dbFile,
        )
//            .createFromAsset("database/countries.db")
            .fallbackToDestructiveMigration(true)
            .setDriver(_root_ide_package_.androidx.sqlite.driver.bundled.BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO).build()
    }

    single { com.example.clean.entry.feature_auth.util.PhoneNumberUtil() }
}