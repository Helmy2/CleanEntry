package com.example.clean.entry.auth.presentation.country_code_picker

import com.example.clean.entry.auth.domain.model.Country
import com.example.clean.entry.core.mvi.Reducer
import org.jetbrains.compose.resources.StringResource

/**
 * Defines the contract for the CountryCodePicker screen and acts as its Reducer.
 */
object CountryCodePickerReducer :
    Reducer<CountryCodePickerReducer.State, CountryCodePickerReducer.Event, CountryCodePickerReducer.Effect> {

    data class State(
        val searchQuery: String = "",
        val selectedCountryCode: String? = null,
        val errorMessage: StringResource? = null,
        val countries: List<Country> = emptyList(),
    ) : Reducer.ViewState

    sealed interface Event : Reducer.ViewEvent {
        data object LoadCountries : Event

        data class InitCountrySelectedCode(val code: String) : Event
        data class CountrySelectedCode(val code: String) : Event
        data class LoadCountriesListSuccess(val countries: List<Country>) : Event
        data class LoadCountriesFailed(val errorMessage: StringResource) : Event
        data class SearchQueryChanged(val query: String) : Event

        data object BackButtonClicked : Event

    }

    sealed interface Effect : Reducer.ViewEffect
    override fun reduce(
        previousState: State,
        event: Event
    ): Pair<State, Nothing?> {
        return when (event) {
            is Event.LoadCountriesListSuccess -> {
                previousState.copy(
                    errorMessage = null,
                    countries = event.countries
                ) to null
            }

            is Event.CountrySelectedCode -> {
                previousState.copy(selectedCountryCode = event.code) to null
            }

            is Event.InitCountrySelectedCode -> {
                previousState.copy(selectedCountryCode = event.code) to null
            }

            is Event.LoadCountriesFailed -> {
                previousState.copy(errorMessage = event.errorMessage) to null
            }

            is Event.SearchQueryChanged -> {
                previousState.copy(searchQuery = event.query) to null
            }

            else -> {
                previousState to null
            }
        }
    }
}