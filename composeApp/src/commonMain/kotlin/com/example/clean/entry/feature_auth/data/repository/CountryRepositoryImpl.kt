package com.example.clean.entry.feature_auth.data.repository

import com.example.clean.entry.core.util.runCatchingOnIO
import com.example.clean.entry.feature_auth.data.source.local.CountryLocalDataSource
import com.example.clean.entry.feature_auth.data.source.remote.CountryRemoteDataSource
import com.example.clean.entry.feature_auth.data.toCountry
import com.example.clean.entry.feature_auth.data.toEntity
import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map


/**
 * The concrete implementation of the CountryRepository.
 * It is responsible for orchestrating data from different data sources (remote, local).
 * For now, it only fetches from the remote data source.
 */
class CountryRepositoryImpl(
    private val remoteDataSource: CountryRemoteDataSource,
    private val localDataSource: CountryLocalDataSource
) : CountryRepository {
    override fun getCountries(query: String): Flow<List<Country>> = channelFlow {
        runCatchingOnIO {
            val cachedCountries = localDataSource.getCountries(query)
                .map { it ->
                    it.map {
                        it.toCountry()
                    }
                }

            cachedCountries.collectLatest {
                trySend(it)
            }
        }

        runCatchingOnIO {
            remoteDataSource.getCountries().getOrThrow().let { freshCountries ->
                trySend(freshCountries.filter { it.name.contains(query, ignoreCase = true) })
                localDataSource.insertCountries(freshCountries.map { it.toEntity() })
            }
        }
    }

    override suspend fun getCountry(code: String): Result<Country> {
        return runCatchingOnIO {
            runCatchingOnIO {
                localDataSource.getCountry(code)?.toCountry()
                    ?: throw Exception("Country not found Locally")
            }.getOrNull() ?: remoteDataSource.getCountries().getOrThrow()
                .firstOrNull { it.code == code } ?: throw Exception("Country not found Remotely")
        }
    }
}
