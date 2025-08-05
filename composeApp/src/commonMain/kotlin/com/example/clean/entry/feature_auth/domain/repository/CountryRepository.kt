package com.example.clean.entry.feature_auth.domain.repository

import androidx.paging.PagingData
import com.example.clean.entry.feature_auth.domain.model.Country
import kotlinx.coroutines.flow.Flow

/**
 * The repository interface for fetching country data.
 * This lives in the domain layer and has no knowledge of the data source.
 */
interface CountryRepository {
    fun getCountries(query: String): Flow<PagingData<Country>>
    suspend fun getCountry(code: String): Result<Country>
}
