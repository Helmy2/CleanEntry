package com.example.clean.entry.feature_auth.presentation.country_code_picker

import androidx.paging.PagingData
import com.example.clean.entry.core.domain.model.Status
import com.example.clean.entry.core.domain.model.StringResource
import com.example.clean.entry.core.mvi.Reducer
import com.example.clean.entry.feature_auth.domain.model.Country
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Defines the contract for the CountryCodePicker screen and acts as its Reducer.
 */
object CountryCodePickerReducer :
    Reducer<CountryCodePickerReducer.State, CountryCodePickerReducer.Event, CountryCodePickerReducer.Effect> {

    data class State(
        val searchQuery: String = "",
        val selectedCountryCode: String? = null,
        val status: Status = Status.Loading,
        val countryFlow: Flow<PagingData<Country>> = flowOf(),
    ) : Reducer.ViewState

    sealed interface Event : Reducer.ViewEvent {
        data object LoadCountries : Event
        data class CountrySelectedCode(val code: String) : Event
        data class CountryPagingDataFlow(val countryFlow: Flow<PagingData<Country>>) : Event
        data class LoadCountriesFailed(val errorMessage: StringResource) : Event
        data class SearchQueryChanged(val query: String) : Event
        data class NavigateBackWithResult(val country: Country) : Event
    }

    sealed interface Effect : Reducer.ViewEffect {
        data class NavigateBackWithResult(val country: Country) : Effect
    }

    override fun reduce(
        previousState: State,
        event: Event
    ): Pair<State, Effect?> {
        return when (event) {
            is Event.CountryPagingDataFlow -> {
                previousState.copy(
                    status = Status.Idle,
                    countryFlow = event.countryFlow
                ) to null
            }

            is Event.CountrySelectedCode -> {
                previousState.copy(selectedCountryCode = event.code) to null
            }

            is Event.LoadCountriesFailed -> {
                previousState.copy(status = Status.Error(event.errorMessage)) to null
            }

            is Event.SearchQueryChanged -> {
                previousState.copy(searchQuery = event.query) to null
            }

            is Event.NavigateBackWithResult -> {
                previousState to Effect.NavigateBackWithResult(event.country)
            }

            else -> {
                previousState to null
            }
        }
    }
}
