package com.example.clean.entry.feature_auth.data.source.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.clean.entry.core.util.IO
import com.example.clean.entry.db.CountryEntity
import com.example.clean.entry.db.CountryEntityQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/**
 * The local data source for managing country data in the Room database.
 *
 * @param countryQuery The Data Access Object for the countries table.
 */
class CountryLocalDataSource(private val countryQuery: CountryEntityQueries) {


    fun getCountries(query: String): Flow<List<CountryEntity>> {
        return countryQuery.searchByName(query).asFlow().mapToList(
            context = Dispatchers.IO
        )
    }

    /**
     * Inserts a list of countries into the database.
     */
    fun insertCountries(countries: List<CountryEntity>) {
        countryQuery.transaction {
            countries.forEach { country ->
                countryQuery.insert(country)
            }
        }
    }

    fun getCountry(code: String): CountryEntity? {
        return countryQuery.selectByCode(code).executeAsOneOrNull()
    }
}
