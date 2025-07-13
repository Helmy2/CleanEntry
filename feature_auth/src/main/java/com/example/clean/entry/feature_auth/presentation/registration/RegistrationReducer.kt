package com.example.clean.entry.feature_auth.presentation.registration

import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.domain.model.ValidationResult
import com.example.clean.entry.mvi.Reducer
import com.example.clean.entry.util.StringResource

/**
 * Defines the contract for the Registration screen and also acts as the Reducer
 * for its state transformations.
 */
object RegistrationReducer :
    Reducer<RegistrationReducer.State, RegistrationReducer.Event, RegistrationReducer.Effect> {

    data class State(
        val firstName: String = "",
        val firstNameError: StringResource? = null,
        val surname: String = "",
        val surnameError: StringResource? = null,
        val email: String = "",
        val emailError: StringResource? = null,
        val phone: String = "",
        val phoneError: StringResource? = null,
        val isLoading: Boolean = false,
        val selectedCountryCode: String = "EG",
        val selectedCountryDialCode: String = "+20",
        val selectedCountryFlag: String = "🇪🇬"
    ) : Reducer.ViewState {
        val isContinueButtonEnabled
            get() = firstName.isNotBlank() && firstNameError == null && surname.isNotBlank() && surnameError == null && email.isNotBlank() && emailError == null && phone.isNotBlank() && phoneError == null
    }

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
        previousState: State, event: Event
    ): Pair<State, Effect?> {
        return when (event) {
            is Event.FirstNameUpdated -> {
                previousState.copy(
                    firstName = event.value, firstNameError = event.result.errorMessage
                ) to null
            }

            is Event.SurnameUpdated -> {
                previousState.copy(
                    surname = event.value, surnameError = event.result.errorMessage
                ) to null
            }

            is Event.EmailUpdated -> {
                previousState.copy(
                    email = event.value, emailError = event.result.errorMessage
                ) to null
            }

            is Event.PhoneUpdated -> {
                previousState.copy(
                    phone = event.value, phoneError = event.result.errorMessage
                ) to null
            }

            is Event.CountrySelected -> {
                previousState.copy(
                    selectedCountryCode = event.result.code,
                    selectedCountryDialCode = event.result.dialCode,
                    selectedCountryFlag = event.result.flagEmoji
                ) to null
            }

            is Event.PhoneChanged -> {
                previousState.copy(phone = event.value) to null
            }

            is Event.Submit -> {
                previousState.copy(isLoading = true) to null
            }

            is Event.RegistrationFinished -> {
                previousState.copy(isLoading = false) to Effect.RegistrationSuccess
            }

            else -> previousState to null
        }

    }
}
