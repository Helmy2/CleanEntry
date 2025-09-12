package com.example.clean.entry.shared.data.source.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.clean.entry.db.AppDatabase
import java.util.Properties

/**
 * The JVM-specific `actual` implementation for the DatabaseDriverFactory.
 */
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return JdbcSqliteDriver("jdbc:sqlite:countries.db", Properties(), AppDatabase.Schema)
    }
}