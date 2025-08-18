package com.example.clean.entry.feature_auth.data.repository

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.map
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val PAGE_SIZE = 10

/**
 * The concrete implementation of the CountryRepository.
 * It is responsible for orchestrating data from different data sources (remote, local).
 * For now, it only fetches from the remote data source.
 */
class CountryRepositoryImpl(
    private val remoteDataSource: CountryRemoteDataSource,
    private val localDataSource: CountryLocalDataSource
) : CountryRepository {
    override fun getPagingCountries(query: String): Flow<PagingData<Country>> = channelFlow {
        val cachedCountries = Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { localDataSource.getPagingCountries(query) }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toCountry()
            }
        }

        trySend(cachedCountries.first())

        runCatchingOnIO {
            remoteDataSource.getCountries().getOrThrow().let { freshCountries ->
                localDataSource.insertCountries(freshCountries.map { it.toEntity() })
            }
        }

        cachedCountries.collectLatest {
            trySend(it)
        }
    }

    override fun getCountries(query: String): Flow<List<Country>> = channelFlow {
        val cachedCountries = localDataSource.getCountries(query)
            .map { it ->
                it.map {
                    it.toCountry()
                }
            }

        trySend(cachedCountries.first())

        runCatchingOnIO {
            remoteDataSource.getCountries().getOrThrow().let { freshCountries ->
                localDataSource.insertCountries(freshCountries.map { it.toEntity() })
            }
        }

        cachedCountries.collectLatest {
            trySend(it)
        }
    }

    override suspend fun getCountry(code: String): Result<Country> {
        return runCatchingOnIO {
            localDataSource.getCountry(code)?.toCountry() ?: throw Exception("Country not found")
        }
    }
}
