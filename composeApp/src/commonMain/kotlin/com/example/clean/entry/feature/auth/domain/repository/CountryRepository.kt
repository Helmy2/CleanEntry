package com.example.clean.entry.feature.auth.domain.repository

import com.example.clean.entry.feature.auth.domain.model.Country
import kotlinx.coroutines.flow.Flow

/**
 * The repository interface for fetching country data.
 * This lives in the domain layer and has no knowledge of the data source.
 */
interface CountryRepository {
    fun getCountries(query: String): Flow<List<Country>>

    suspend fun getCountry(code: String): Result<Country>
}
