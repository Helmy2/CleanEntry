package com.example.clean.entry.auth.presentation.country_code_picker

import androidx.lifecycle.viewModelScope
import cleanentry.feature.auth.generated.resources.Res
import cleanentry.feature.auth.generated.resources.country_code_picker_error
import com.example.clean.entry.auth.domain.repository.CountryRepository
import com.example.clean.entry.auth.navigation.CounterCodeResult
import com.example.clean.entry.core.mvi.BaseViewModel
import com.example.clean.entry.core.navigation.AppNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CountryCodePickerViewModel(
    val countryCode: String? = null,
    val countryRepository: CountryRepository,
    private val navigator: AppNavigator,
) : BaseViewModel<CountryCodePickerReducer.State, CountryCodePickerReducer.Event, CountryCodePickerReducer.Effect>(
    reducer = CountryCodePickerReducer, initialState = CountryCodePickerReducer.State()
) {

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val countryFlow = state
        .debounce(300L)
        .map { it.searchQuery }
        .flatMapLatest { query ->
            countryRepository.getCountries(query)
        }
        .catch {
            handleEvent(CountryCodePickerReducer.Event.LoadCountriesFailed(Res.string.country_code_picker_error))
        }

    override suspend fun initialDataLoad() {
        super.initialDataLoad()
        countryCode?.let {
            handleEvent(CountryCodePickerReducer.Event.InitCountrySelectedCode(it))
        }
        countryFlow.collect {
            handleEvent(CountryCodePickerReducer.Event.LoadCountriesListSuccess(it))
        }
    }


    override fun handleEvent(event: CountryCodePickerReducer.Event) {
        when (event) {
            is CountryCodePickerReducer.Event.LoadCountries -> viewModelScope.launch {
                setState(event)
                initialDataLoad()
            }

            is CountryCodePickerReducer.Event.CountrySelectedCode -> viewModelScope.launch {
                countryRepository.getCountry(event.code).onSuccess {
                    navigator.navigateBackWithResult(
                        CounterCodeResult(it)
                    )
                }
            }

            is CountryCodePickerReducer.Event.BackButtonClicked -> {
                navigator.navigateBack()
            }

            else -> setState(event)
        }
    }
}
