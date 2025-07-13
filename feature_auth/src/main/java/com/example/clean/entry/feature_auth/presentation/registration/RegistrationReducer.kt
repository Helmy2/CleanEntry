package com.example.clean.entry.feature_auth.presentation.registration

import com.example.clean.entry.mvi.Reducer
import com.example.clean.entry.util.StringResource
import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.domain.model.ValidationResult

/**
 * Defines the contract for the Registration screen and also acts as the Reducer
 * for its state transformations.
 */
object RegistrationReducer : Reducer<RegistrationReducer.State, RegistrationReducer.Event, RegistrationReducer.Effect> {

    data class State(
        val firstName: String = "",
        val firstNameError: StringResource? = null,
        val surname: String = "",
        val surnameError: StringResource? = null,
        val email: String = "",
        val emailError: StringResource? = null,
        val phone: String = "",
        val phoneError: StringResource? = null,
        val isContinueButtonEnabled: Boolean = false,
        val isLoading: Boolean = false,
        val selectedCountryCode: String = "EG",
        val selectedCountryDialCode: String = "+20",
        val selectedCountryFlag: String = "🇪🇬"
    ) : Reducer.ViewState

    sealed interface Event : Reducer.ViewEvent {
        // UI Events
        data class FirstNameChanged(val value: String) : Event
        data class SurnameChanged(val value: String) : Event
        data class EmailChanged(val value: String) : Event
        data class PhoneChanged(val value: String) : Event
        data object Submit : Event
        data class CountrySelected(val result: Country) : Event // New event

        // Internal Events
        data class FirstNameUpdated(val value: String, val result: ValidationResult) : Event
        data class SurnameUpdated(val value: String, val result: ValidationResult) : Event
        data class EmailUpdated(val value: String, val result: ValidationResult) : Event
        data class PhoneUpdated(val value: String, val result: ValidationResult) : Event
        data object RegistrationFinished : Event
    }

    sealed interface Effect : Reducer.ViewEffect {
        data object RegistrationSuccess : Effect
    }

    override fun reduce(
        previousState: State,
        event: Event
    ): Pair<State, Effect?> {
        val newState = when (event) {
            is Event.FirstNameUpdated -> {
                previousState.copy(firstName = event.value, firstNameError = event.result.errorMessage)
            }
            is Event.SurnameUpdated -> {
                previousState.copy(surname = event.value, surnameError = event.result.errorMessage)
            }
            is Event.EmailUpdated -> {
                previousState.copy(email = event.value, emailError = event.result.errorMessage)
            }
            is Event.PhoneUpdated -> {
                previousState.copy(phone = event.value, phoneError = event.result.errorMessage)
            }
            is Event.CountrySelected -> {
                previousState.copy(
                    selectedCountryCode = event.result.code,
                    selectedCountryDialCode = event.result.dialCode,
                    selectedCountryFlag = event.result.flagEmoji
                )
            }
            is Event.PhoneChanged -> {
                previousState.copy(phone = event.value)
            }
            is Event.Submit -> {
                previousState.copy(isLoading = true)
            }
            is Event.RegistrationFinished -> {
                previousState.copy(isLoading = false)
            }
            else -> previousState
        }

        val isFormValid = newState.firstName.isNotBlank() && newState.firstNameError == null &&
                newState.surname.isNotBlank() && newState.surnameError == null &&
                newState.email.isNotBlank() && newState.emailError == null &&
                newState.phone.isNotBlank() && newState.phoneError == null

        return newState.copy(isContinueButtonEnabled = isFormValid) to null
    }
}
