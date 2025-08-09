package com.example.clean.entry.feature_auth.data.source.remote

import com.apollographql.apollo.ApolloClient
import com.example.clean.entry.core.util.runCatchingOnIO
import com.example.clean.entry.CountriesQuery
import com.example.clean.entry.feature_auth.data.toCountry
import com.example.clean.entry.feature_auth.domain.model.Country

/**
 * The remote data source for fetching country data from the GraphQL API.
 */
class CountryRemoteDataSource(
    private val apolloClient: ApolloClient
) {
   suspend fun getCountries(): Result<List<Country>> = runCatchingOnIO {
        val response = apolloClient.query(CountriesQuery()).execute()
        if (response.hasErrors()) {
            throw Exception(response.errors?.firstOrNull()?.message ?: "GraphQL error")
        }
        response.dataAssertNoErrors.countries.map { it.toCountry() }
    }
}
