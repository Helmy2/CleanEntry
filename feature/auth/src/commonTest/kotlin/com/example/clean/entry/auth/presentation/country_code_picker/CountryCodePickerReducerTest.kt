package com.example.clean.entry.auth.presentation.country_code_picker

import com.example.clean.entry.auth.domain.model.Country
import kotlin.test.Test
import kotlin.test.assertEquals

class CountryCodePickerReducerTest {

    private val reducer = CountryCodePickerReducer
    private val initialState = CountryCodePickerReducer.State()

    @Test
    fun `given LoadCountriesListSuccess event then state is updated with countries`() {
        // Arrange
        val countries = listOf(Country.Egypt, Country.Egypt)
        val event = CountryCodePickerReducer.Event.LoadCountriesListSuccess(countries)

        // Act
        val (newState, _) = reducer.reduce(initialState, event)

        // Assert
        assertEquals(countries, newState.countries)
    }

    @Test
    fun `given InitCountrySelectedCode event then selected country code is set`() {
        // Arrange
        val code = "US"
        val event = CountryCodePickerReducer.Event.InitCountrySelectedCode(code)

        // Act
        val (newState, _) = reducer.reduce(initialState, event)

        // Assert
        assertEquals(code, newState.selectedCountryCode)
    }

    @Test
    fun `given SearchQueryChanged event then search query is updated`() {
        // Arrange
        val query = "United"
        val event = CountryCodePickerReducer.Event.SearchQueryChanged(query)

        // Act
        val (newState, _) = reducer.reduce(initialState, event)

        // Assert
        assertEquals(query, newState.searchQuery)
    }

    @Test
    fun `filteredCountries should be updated based on search query`() {
        // Arrange
        val countries = listOf(Country.Egypt)
        val stateWithCountries = initialState.copy(countries = countries)

        // Act
        val (stateAfterSearch, _) = reducer.reduce(
            stateWithCountries,
            CountryCodePickerReducer.Event.SearchQueryChanged("Eg")
        )

        // Assert
        assertEquals(1, stateAfterSearch.countries.size)
        assertEquals(Country.Egypt, stateAfterSearch.countries.first())
    }
}
