package com.example.clean.entry.feature_auth.data.source.remote

import com.apollographql.apollo.ApolloClient
import com.example.clean.entry.feature_auth.CountriesQuery
import com.example.clean.entry.feature_auth.data.toCountry
import com.example.clean.entry.feature_auth.domain.model.Country
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * The remote data source for fetching country data from the GraphQL API.
 */
class CountryRemoteDataSource(
    private val apolloClient: ApolloClient
) {
    fun getCountries(): Flow<Result<List<Country>>> = flow {
        val result = runCatching {
            val response = apolloClient.query(CountriesQuery()).execute()
            if (response.hasErrors()) {
                throw Exception(response.errors?.firstOrNull()?.message ?: "GraphQL error")
            }
            response.dataAssertNoErrors.countries.map { it.toCountry() }
        }.onFailure {
            it.printStackTrace()
        }
        emit(result)
    }
}
