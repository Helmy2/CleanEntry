package com.example.clean.entry.feature.auth.presentation.country_code_picker

import com.example.clean.entry.core.domain.model.Status
import com.example.clean.entry.core.domain.model.StringResource
import com.example.clean.entry.core.mvi.Reducer
import com.example.clean.entry.feature.auth.domain.model.Country
import com.example.clean.entry.feature.auth.presentation.registration.RegistrationReducer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Defines the contract for the CountryCodePicker screen and acts as its Reducer.
 */
object CountryCodePickerReducer :
    Reducer<CountryCodePickerReducer.State, CountryCodePickerReducer.Event, Nothing> {

    data class State(
        val searchQuery: String = "",
        val selectedCountryCode: String? = null,
        val status: Status = Status.Loading,
        val countryFlow: Flow<List<Country>> = flowOf(),
    ) : Reducer.ViewState

    sealed interface Event : Reducer.ViewEvent {
        data object LoadCountries : Event

        data class InitCountrySelectedCode(val code: String) : Event
        data class CountrySelectedCode(val code: String) : Event
        data class CountryDataFlow(val countryFlow: Flow<List<Country>>) : Event
        data class LoadCountriesFailed(val errorMessage: StringResource) : Event
        data class SearchQueryChanged(val query: String) : Event

        data object BackButtonClicked : Event

    }

    override fun reduce(
        previousState: State,
        event: Event
    ): Pair<State, Nothing?> {
        return when (event) {
            is Event.CountryDataFlow -> {
                previousState.copy(
                    status = Status.Idle,
                    countryFlow = event.countryFlow
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