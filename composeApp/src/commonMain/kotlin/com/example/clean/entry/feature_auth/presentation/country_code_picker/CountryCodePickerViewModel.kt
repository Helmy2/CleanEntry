package com.example.clean.entry.feature_auth.presentation.country_code_picker

import androidx.lifecycle.viewModelScope
import com.example.clean.entry.core.domain.model.StringResource
import com.example.clean.entry.core.mvi.BaseViewModel
import com.example.clean.entry.feature_auth.domain.repository.CountryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class CountryCodePickerViewModel(
    val countryRepository: CountryRepository,
) : BaseViewModel<CountryCodePickerReducer.State, CountryCodePickerReducer.Event, CountryCodePickerReducer.Effect>(
    reducer = CountryCodePickerReducer, initialState = CountryCodePickerReducer.State()
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val countryFlow = state
        .debounce(300L)
        .map { it.searchQuery }
        .flatMapLatest { query ->
            countryRepository.getCountries(query)
        }
        .catch {
            val errorMessage =
                StringResource.FromString("Failed to load countries. Please try again.")
            setState(CountryCodePickerReducer.Event.LoadCountriesFailed(errorMessage))
        }

    override suspend fun initialDataLoad() {
        setState(CountryCodePickerReducer.Event.CountryDataFlow(countryFlow))
    }


    override fun handleEvent(event: CountryCodePickerReducer.Event) {
        when (event) {
            is CountryCodePickerReducer.Event.LoadCountries -> viewModelScope.launch {
                setState(event)
                initialDataLoad()
            }

            is CountryCodePickerReducer.Event.CountrySelectedCode -> viewModelScope.launch {
                countryRepository.getCountry(event.code).onSuccess {
                    setState(
                        CountryCodePickerReducer.Event.NavigateBackWithResult(it)
                    )
                }
            }

            else -> setState(event)
        }
    }
}
