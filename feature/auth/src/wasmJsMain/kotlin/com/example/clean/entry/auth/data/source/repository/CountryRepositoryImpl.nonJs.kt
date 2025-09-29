package com.example.clean.entry.auth.data.source.repository

import com.example.clean.entry.auth.data.source.remote.CountryRemoteDataSource
import com.example.clean.entry.auth.domain.model.Country
import com.example.clean.entry.auth.domain.repository.CountryRepository
import com.example.clean.entry.core.util.runCatchingOnIO
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
