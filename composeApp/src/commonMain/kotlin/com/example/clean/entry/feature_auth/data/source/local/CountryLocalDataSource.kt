package com.example.clean.entry.feature_auth.data.source.local

import androidx.paging.PagingSource
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
    fun getPagingCountries(query: String): PagingSource<Int, CountryEntity> {
        return countryDao.getPagingCountries(query)
    }

    fun getCountries(query: String): Flow<List<CountryEntity>> {
        return countryDao.getCountries(query)
    }

    /**
     * Inserts a list of countries into the database.
     */
    suspend fun insertCountries(countries: List<CountryEntity>) {
        countryDao.insertAll(countries)
    }

    suspend fun getCountry(code: String): CountryEntity? {
        return countryDao.getCountry(code)
    }
}
