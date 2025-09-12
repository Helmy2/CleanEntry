package com.example.clean.entry.feature.auth.data.source.repository

import com.example.clean.entry.core.util.runCatchingOnIO
import com.example.clean.entry.feature.auth.data.source.remote.CountryRemoteDataSource
import com.example.clean.entry.feature.auth.domain.model.Country
import com.example.clean.entry.feature.auth.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow


class CountryRepositoryImpl(
    private val remoteDataSource: CountryRemoteDataSource,
) : CountryRepository {
    override fun getCountries(query: String): Flow<List<Country>> = channelFlow {
        runCatchingOnIO {
            remoteDataSource.getCountries().getOrThrow().let { freshCountries ->
                trySend(freshCountries.filter { it.name.contains(query, ignoreCase = true) })
            }
        }
    }

    override suspend fun getCountry(code: String): Result<Country> {
        return runCatchingOnIO {
            remoteDataSource.getCountryByCode(code).getOrThrow()
        }
    }
}
