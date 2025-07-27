package com.example.clean.entry.feature_auth.data.repository

import com.example.clean.entry.core.util.runCatchingOnIO
import com.example.clean.entry.feature_auth.data.source.local.CountryLocalDataSource
import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.domain.repository.CountryRepository
import com.example.clean.entry.feature_auth.data.source.remote.CountryRemoteDataSource
import com.example.clean.entry.feature_auth.data.toCountry
import com.example.clean.entry.feature_auth.data.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * The concrete implementation of the CountryRepository.
 * It is responsible for orchestrating data from different data sources (remote, local).
 * For now, it only fetches from the remote data source.
 */
class CountryRepositoryImpl(
    private val remoteDataSource: CountryRemoteDataSource,
    private val localDataSource: CountryLocalDataSource
) : CountryRepository {
    override suspend fun getCountries(): Flow<Result<List<Country>>> = flow {
        val cachedCountries = localDataSource.getCountries().map { entities ->
            entities.map { it.toCountry() }
        }
        emit(Result.success(cachedCountries.first()))

        runCatchingOnIO {
            remoteDataSource.getCountries().getOrThrow().let { freshCountries ->
                localDataSource.insertCountries(freshCountries.map { it.toEntity() })
            }
        }

        cachedCountries.collectLatest {
            emit(Result.success(it))
        }
    }
}
