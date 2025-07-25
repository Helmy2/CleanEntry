package com.example.clean.entry.feature_auth.presentation.country_code_picker

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.navigation.AuthDestination
import com.example.clean.entry.core.mvi.BaseViewModel
import com.example.clean.entry.feature_auth.domain.repository.CountryRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class CountryCodePickerViewModel(
    val countryRepository: CountryRepository,
    val savedStateHandle: SavedStateHandle
) : BaseViewModel<CountryCodePickerReducer.State, CountryCodePickerReducer.Event, CountryCodePickerReducer.Effect>(
    reducer = CountryCodePickerReducer, initialState = CountryCodePickerReducer.State()
) {

    override suspend fun initialDataLoad() {
        countryRepository.getCountries().first().fold(
            onSuccess = { countries ->
                setState(CountryCodePickerReducer.Event.CountriesLoaded(countries))
            },
            onFailure = {
                // TODO show error
                Log.d("TAG", ": $it")
            }
        )
        val selectedCountryCode = savedStateHandle.toRoute<AuthDestination.CountryCodePicker>().code
        setState(CountryCodePickerReducer.Event.CountrySelectedCode(selectedCountryCode))
    }


    override fun handleEvent(event: CountryCodePickerReducer.Event) {
        when (event) {
            else -> setState(event)
        }
    }
}
