package com.example.clean.entry.feature_auth.data.source.local

import com.example.clean.entry.feature_auth.data.source.local.dao.CountryDao
import com.example.clean.entry.feature_auth.data.source.local.entity.CountryEntity
import kotlinx.coroutines.flow.Flow

/**
 * The local data source for managing country data in the Room database.
 *
 * @param countryDao The Data Access Object for the countries table.
 */
class CountryLocalDataSource(private val countryDao: CountryDao) {

    /**
     * Retrieves all countries from the database as a Flow.
     */
    fun getCountries(): Flow<List<CountryEntity>> {
        return countryDao.getAllCountries()
    }

    /**
     * Inserts a list of countries into the database.
     */
    suspend fun insertCountries(countries: List<CountryEntity>) {
        countryDao.insertAll(countries)
    }
}
