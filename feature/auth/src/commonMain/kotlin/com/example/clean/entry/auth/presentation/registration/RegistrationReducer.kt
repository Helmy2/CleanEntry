package com.example.clean.entry.auth.presentation.registration

import com.example.clean.entry.auth.domain.model.Country
import com.example.clean.entry.core.domain.model.StringResource
import com.example.clean.entry.core.domain.model.ValidationResult
import com.example.clean.entry.core.mvi.Reducer

/**
 * Defines the contract for the Registration screen and also acts as the Reducer
 * for its state transformations.
 */
object RegistrationReducer :
    Reducer<RegistrationReducer.State, RegistrationReducer.Event, Nothing> {

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
        val selectedCountry: Country = Country.Egypt,
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
        data class CountrySelected(val country: Country) : Event // New event

        // Internal Events
        data class FirstNameUpdated(val value: String, val result: ValidationResult) : Event
        data class SurnameUpdated(val value: String, val result: ValidationResult) : Event
        data class EmailUpdated(val value: String, val result: ValidationResult) : Event
        data class PhoneUpdated(val value: String, val result: ValidationResult) : Event

        data object RegistrationFinished : Event

        data object BackButtonClicked : Event

        data object CountryButtonClick : Event
    }

    override fun reduce(
        previousState: State, event: Event
    ): Pair<State, Nothing?> {
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
                    selectedCountry = event.country,
                ) to null
            }

            is Event.PhoneChanged -> {
                previousState.copy(phone = event.value) to null
            }

            is Event.Submit -> {
                previousState.copy(isLoading = true) to null
            }

            is Event.RegistrationFinished -> {
                previousState.copy(isLoading = false) to null
            }

            else -> previousState to null
        }

    }
}
