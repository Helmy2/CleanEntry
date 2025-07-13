package com.example.clean.entry.feature_auth.presentation.registration

import androidx.lifecycle.viewModelScope
import com.example.clean.entry.mvi.BaseViewModel
import com.example.clean.entry.feature_auth.domain.usecase.ValidateEmailUseCase
import com.example.clean.entry.feature_auth.domain.usecase.ValidateFirstNameUseCase
import com.example.clean.entry.feature_auth.domain.usecase.ValidatePhoneUseCase
import com.example.clean.entry.feature_auth.domain.usecase.ValidateSurnameUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val validateFirstNameUseCase: ValidateFirstNameUseCase,
    private val validateSurnameUseCase: ValidateSurnameUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePhoneUseCase: ValidatePhoneUseCase,
) : BaseViewModel<RegistrationReducer.State, RegistrationReducer.Event, RegistrationReducer.Effect>(
    reducer = RegistrationReducer,
    initialState = RegistrationReducer.State()
) {

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
                val regionCode = state.value.selectedCountryCode
                val result = validatePhoneUseCase(event.value, regionCode)
                setState(RegistrationReducer.Event.PhoneUpdated(event.value, result))
            }

            is RegistrationReducer.Event.Submit -> {
                submitRegistration()
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
