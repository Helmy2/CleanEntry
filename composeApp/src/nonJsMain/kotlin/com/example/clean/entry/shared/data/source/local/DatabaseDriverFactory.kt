package com.example.clean.entry.shared.data.source.local

import app.cash.sqldelight.db.SqlDriver

/**
 * An `expect` declaration for a platform-specific factory that creates
 * an SQLDelight database driver.
 */
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}