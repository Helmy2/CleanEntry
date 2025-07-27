package com.example.clean.entry.feature_auth.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clean.entry.feature_auth.data.source.local.entity.CountryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the countries table.
 * Provides methods for interacting with the local database.
 */
@Dao
interface CountryDao {

    /**
     * Inserts a list of countries into the database. If a country already exists,
     * it will be replaced.
     *
     * @param countries The list of CountryEntity objects to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<CountryEntity>)

    /**
     * Retrieves all countries from the database as a Flow.
     * The Flow will automatically emit a new list whenever the data changes.
     *
     * @return A Flow emitting a list of all CountryEntity objects.
     */
    @Query("SELECT * FROM countries ORDER BY name ASC")
    fun getAllCountries(): Flow<List<CountryEntity>>
}
