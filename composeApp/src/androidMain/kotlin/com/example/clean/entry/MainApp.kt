package com.example.clean.entry

import android.app.Application
import com.example.clean.entry.feature_auth.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@MainApp)
        }
    }
}