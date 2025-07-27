package com.example.clean.entry.feature_auth.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.clean.entry.feature_auth.data.source.local.dao.CountryDao
import com.example.clean.entry.feature_auth.data.source.local.entity.CountryEntity

/**
 * The main Room database class for the feature.
 */
@Database(
    entities = [CountryEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Provides an instance of the CountryDao.
     */
    abstract fun countryDao(): CountryDao
}
