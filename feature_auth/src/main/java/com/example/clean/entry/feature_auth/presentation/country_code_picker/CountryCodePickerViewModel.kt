package com.example.clean.entry.feature_auth.presentation.country_code_picker

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.navigation.AuthDestination
import com.example.clean.entry.mvi.BaseViewModel
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
class CountryCodePickerViewModel(
    val savedStateHandle : SavedStateHandle
) : BaseViewModel<CountryCodePickerReducer.State, CountryCodePickerReducer.Event, CountryCodePickerReducer.Effect>(
    reducer = CountryCodePickerReducer, initialState = CountryCodePickerReducer.State()
) {

    private val allCountries = listOf(
        Country("Egypt", "+20", "EG", "🇪🇬"),
        Country("Saudi Arabia", "+966", "SA", "🇸🇦"),
        Country("United Kingdom", "+44", "GB", "🇬🇧"),
        Country("Canada", "+1", "CA", "🇨🇦"),
        Country("France", "+33", "FR", "🇫🇷"),
        Country("Spain", "+34", "ES", "🇪🇸"),
        Country("Italy", "+39", "IT", "🇮🇹"),
        Country("United States", "+1", "US", "🇺🇸")
    )


    override suspend fun initialDataLoad() {
        setState(CountryCodePickerReducer.Event.CountriesLoaded(allCountries))
        val selectedCountryCode = savedStateHandle.toRoute<AuthDestination.CountryCodePicker>().code
        setState(CountryCodePickerReducer.Event.CountrySelectedCode(selectedCountryCode))
    }


    override fun handleEvent(event: CountryCodePickerReducer.Event) {
        when (event) {
            else -> setState(event)
        }
    }
}
