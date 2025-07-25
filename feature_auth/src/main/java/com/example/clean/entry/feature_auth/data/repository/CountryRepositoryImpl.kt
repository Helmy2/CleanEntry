package com.example.clean.entry.feature_auth.data.repository

import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.domain.repository.CountryRepository
import com.example.clean.entry.feature_auth.data.source.remote.CountryRemoteDataSource
import kotlinx.coroutines.flow.Flow

/**
 * The concrete implementation of the CountryRepository.
 * It is responsible for orchestrating data from different data sources (remote, local).
 * For now, it only fetches from the remote data source.
 */
class CountryRepositoryImpl(
    private val remoteDataSource: CountryRemoteDataSource
) : CountryRepository {
    override fun getCountries(): Flow<Result<List<Country>>> {
        return remoteDataSource.getCountries()
    }
}
