package com.example.clean.entry.shared.data.source.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.clean.entry.db.AppDatabase

/**
 * The Android-specific `actual` implementation for the DatabaseDriverFactory.
 */
actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Companion.Schema, context, "countries.db")
    }
}