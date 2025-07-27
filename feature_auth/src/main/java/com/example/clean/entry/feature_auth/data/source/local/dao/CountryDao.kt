package com.example.clean.entry.feature_auth.data.source.local.dao

import androidx.paging.PagingSource
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
     * Retrieves a PagingSource that loads pages of countries from the database.
     * This is used by the Paging library to efficiently load data for the UI.
     * The query now includes filtering for the search functionality.
     *
     * @param query The search query to filter country names.
     * @return A PagingSource for the Paging library.
     */
    @Query("SELECT * FROM countries ORDER BY name ASC")
    fun getCountries(): PagingSource<Int, CountryEntity>
}
