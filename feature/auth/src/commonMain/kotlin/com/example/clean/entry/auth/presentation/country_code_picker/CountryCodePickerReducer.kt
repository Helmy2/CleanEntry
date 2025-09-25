package com.example.clean.entry.auth.presentation.country_code_picker

import com.example.clean.entry.auth.domain.model.Country
import com.example.clean.entry.core.domain.model.Status
import com.example.clean.entry.core.mvi.Reducer

/**
 * Defines the contract for the CountryCodePicker screen and acts as its Reducer.
 */
object CountryCodePickerReducer :
    Reducer<CountryCodePickerReducer.State, CountryCodePickerReducer.Event, CountryCodePickerReducer.Effect> {

    data class State(
        val searchQuery: String = "",
        val selectedCountryCode: String? = null,
        val status: Status = Status.Loading,
        val countries: List<Country> = emptyList(),
    ) : Reducer.ViewState

    sealed interface Event : Reducer.ViewEvent {
        data object LoadCountries : Event

        data class InitCountrySelectedCode(val code: String) : Event
        data class CountrySelectedCode(val code: String) : Event
        data class LoadCountriesListSuccess(val countries: List<Country>) : Event
        data class LoadCountriesFailed(val errorMessage: String) : Event
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
                    status = Status.Idle,
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
                previousState.copy(status = Status.Error(event.errorMessage)) to null
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