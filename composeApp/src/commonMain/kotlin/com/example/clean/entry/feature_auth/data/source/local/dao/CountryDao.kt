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
     * Retrieves all countries from the database as a Flow.
     *
     * @return A Flow of CountryEntity objects representing the countries.
     */
    @Query("SELECT * FROM countries WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun getPagingCountries(query: String): PagingSource<Int, CountryEntity>


    @Query("SELECT * FROM countries WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun getCountries(query: String): Flow<List<CountryEntity>>

    /**
     * Retrieves a single country from the database based on its code.
     *
     * @param code The code of the country to retrieve.
     * @return The CountryEntity object representing the country, or null if not found.
     */
    @Query("SELECT * FROM countries WHERE code = :code")
    suspend fun getCountry(code: String): CountryEntity?
}
