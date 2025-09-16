package com.example.clean.entry.feature.auth.presentation.registration

import androidx.lifecycle.viewModelScope
import com.example.clean.entry.core.mvi.BaseViewModel
import com.example.clean.entry.feature.auth.domain.model.Country
import com.example.clean.entry.feature.auth.domain.repository.CountryRepository
import com.example.clean.entry.feature.auth.domain.usecase.ValidateEmailUseCase
import com.example.clean.entry.feature.auth.domain.usecase.ValidateFirstNameUseCase
import com.example.clean.entry.feature.auth.domain.usecase.ValidatePhoneUseCase
import com.example.clean.entry.feature.auth.domain.usecase.ValidateSurnameUseCase
import com.example.clean.entry.navigation.AppDestination
import com.example.clean.entry.navigation.AppNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val validateFirstNameUseCase: ValidateFirstNameUseCase,
    private val validateSurnameUseCase: ValidateSurnameUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePhoneUseCase: ValidatePhoneUseCase,
    private val countryRepository: CountryRepository,
    private val navigator: AppNavigator,
) : BaseViewModel<RegistrationReducer.State, RegistrationReducer.Event, Nothing>(
    reducer = RegistrationReducer,
    initialState = RegistrationReducer.State()
) {
    override suspend fun initialDataLoad() {
        super.initialDataLoad()
        viewModelScope.launch {
            navigator.getValue(AppNavigator.Companion.Keys.COUNTER_CODE).collect {
                val country = countryRepository.getCountry(it).getOrNull() ?: Country.Egypt
                handleEvent(RegistrationReducer.Event.CountrySelected(country))
            }
        }
    }

    override fun handleEvent(event: RegistrationReducer.Event) {
        when (event) {
            is RegistrationReducer.Event.FirstNameChanged -> {
                val result = validateFirstNameUseCase(event.value)
                setState(RegistrationReducer.Event.FirstNameUpdated(event.value, result))
            }

            is RegistrationReducer.Event.SurnameChanged -> {
                val result = validateSurnameUseCase(event.value)
                setState(RegistrationReducer.Event.SurnameUpdated(event.value, result))
            }

            is RegistrationReducer.Event.EmailChanged -> {
                val result = validateEmailUseCase(event.value)
                setState(RegistrationReducer.Event.EmailUpdated(event.value, result))
            }

            is RegistrationReducer.Event.PhoneChanged -> {
                val regionCode = state.value.selectedCountry.code
                val result = validatePhoneUseCase(event.value, regionCode)
                setState(RegistrationReducer.Event.PhoneUpdated(event.value, result))
            }

            is RegistrationReducer.Event.Submit -> {
                submitRegistration()
            }

            is RegistrationReducer.Event.BackButtonClicked -> {
                navigator.navigateBack()
            }

            is RegistrationReducer.Event.CountryButtonClick -> {
                navigator.navigate(
                    AppDestination.Auth.CountryCodePicker(state.value.selectedCountry.code)
                )
            }

            is RegistrationReducer.Event.RegistrationFinished -> {
                navigator.navigateAsRoot(AppDestination.Dashboard)
            }

            else -> setState(event)
        }
    }

    private fun submitRegistration() {
        setState(RegistrationReducer.Event.Submit)
        viewModelScope.launch {
            // TODO: Implement registration
            delay(2000)
            setState(RegistrationReducer.Event.RegistrationFinished)
        }
    }
}
