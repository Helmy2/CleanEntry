package com.example.clean.entry.feature_auth.data.source.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.example.clean.entry.feature_auth.data.source.local.dao.CountryDao
import com.example.clean.entry.feature_auth.data.source.local.entity.CountryEntity

/**
 * The main Room database class for the feature.
 */
@Database(
    entities = [CountryEntity::class],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Provides an instance of the CountryDao.
     */
    abstract fun countryDao(): CountryDao
}


@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}