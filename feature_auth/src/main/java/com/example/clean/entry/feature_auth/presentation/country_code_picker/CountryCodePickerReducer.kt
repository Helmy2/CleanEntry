package com.example.clean.entry.feature_auth.presentation.country_code_picker

import com.example.clean.entry.core.domain.model.Status
import com.example.clean.entry.core.domain.model.StringResource
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
        val status: Status = Status.Loading,
        val errorMessage: StringResource? = null
    ) : Reducer.ViewState

    sealed interface Event : Reducer.ViewEvent {
        data object LoadCountries : Event
        data class CountrySelected(val country: Country) : Event
        data class CountrySelectedCode(val code: String) : Event
        data class LoadCountriesSuccess(val countries: List<Country>) : Event
        data class LoadCountriesFailed(val errorMessage: StringResource): Event
    }

    sealed interface Effect : Reducer.ViewEffect {
        data class NavigateBackWithResult(val country: Country) : Effect
    }

    override fun reduce(
        previousState: State,
        event: Event
    ): Pair<State, Effect?> {
        return when (event) {
            is Event.LoadCountriesSuccess -> {
                previousState.copy(
                    status = Status.Idle,
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

            is Event.LoadCountriesFailed ->  {
                previousState.copy(
                    status = Status.Error(event.errorMessage),
                    errorMessage = event.errorMessage
                ) to null
            }

            is Event.LoadCountries -> {
                previousState.copy(
                    status = Status.Loading
                ) to null
            }
        }
    }
}
