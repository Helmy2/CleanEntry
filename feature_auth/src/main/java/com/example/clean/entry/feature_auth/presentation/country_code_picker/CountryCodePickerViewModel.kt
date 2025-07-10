package com.example.clean.entry.feature_auth.presentation.country_code_picker

import com.example.clean.core.mvi.BaseViewModel
import com.example.clean.entry.feature_auth.domain.model.Country
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
class CountryCodePickerViewModel(
    val selectedCountry: Country
) :
    BaseViewModel<CountryCodePickerReducer.State, CountryCodePickerReducer.Event, CountryCodePickerReducer.Effect>(
        reducer = CountryCodePickerReducer,
        initialState = CountryCodePickerReducer.State()
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
        setState(CountryCodePickerReducer.Event.CountrySelected(selectedCountry))
    }


    override fun handleEvent(event: CountryCodePickerReducer.Event) {
        when (event) {
            else -> setState(event)
        }
    }
}
