package com.example.clean.entry.feature_auth.presentation.country_code_picker

import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.presentation.country_code_picker.CountryCodePickerReducer.Effect.NavigateBackWithResult
import com.example.clean.entry.core.mvi.Reducer

/**
 * Defines the contract for the CountryCodePicker screen and acts as its Reducer.
 */
object CountryCodePickerReducer :
    Reducer<CountryCodePickerReducer.State, CountryCodePickerReducer.Event, CountryCodePickerReducer.Effect> {

    data class State(
        val countries: List<Country> = emptyList(),
        val selectedCountry: Country? = null,
        val isLoading: Boolean = true,
    ) : Reducer.ViewState

    sealed interface Event : Reducer.ViewEvent {
        data class CountrySelected(val country: Country) : Event
        data class CountrySelectedCode(val code: String) : Event
        data class CountriesLoaded(val countries: List<Country>) : Event
    }

    sealed interface Effect : Reducer.ViewEffect {
        data class NavigateBackWithResult(val country: Country) : Effect
    }

    override fun reduce(
        previousState: State,
        event: Event
    ): Pair<State, Effect?> {
        return when (event) {
            is Event.CountriesLoaded -> {
                previousState.copy(
                    isLoading = false,
                    countries = event.countries,
                ) to null
            }

            is Event.CountrySelectedCode -> {
                previousState.copy(
                    selectedCountry = previousState.countries.find { it.code == event.code },
                ) to null
            }

            is Event.CountrySelected -> {
                previousState.copy(selectedCountry = event.country) to NavigateBackWithResult(event.country)
            }
        }
    }
}
