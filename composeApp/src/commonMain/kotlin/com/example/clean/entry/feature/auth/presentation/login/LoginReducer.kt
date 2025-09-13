package com.example.clean.entry.feature.auth.presentation.login

import com.example.clean.entry.feature.auth.domain.model.Country
import com.example.clean.entry.core.domain.model.ValidationResult
import com.example.clean.entry.core.mvi.Reducer
import com.example.clean.entry.core.domain.model.StringResource

/**
 * Defines the contract for the Login screen and also acts as the Reducer
 * for its state transformations.
 */
object LoginReducer : Reducer<LoginReducer.State, LoginReducer.Event, Nothing> {

    data class State(
        val phone: String = "",
        val phoneError: StringResource? = null,
        val password: String = "",
        val passwordError: StringResource? = null,
        val isPasswordVisible: Boolean = false,
        val isLoading: Boolean = false,
        val selectedCountry: Country = Country.Egypt,
    ) : Reducer.ViewState {
        val isLoginButtonEnabled
            get() = phone.isNotBlank() && phoneError == null &&
                    password.isNotBlank() && passwordError == null &&
                    !isLoading
    }

    sealed interface Event : Reducer.ViewEvent {
        // UI Events
        data class PhoneChanged(val value: String) : Event
        data class PasswordChanged(val value: String) : Event
        data object TogglePasswordVisibility : Event
        data object LoginClicked : Event
        data class CountrySelected(val country: Country) : Event // New event

        // Internal Events
        data class PhoneUpdated(val value: String, val result: ValidationResult) : Event
        data class PasswordUpdated(val value: String, val result: ValidationResult) : Event
        data object LoginFinished : Event

        data object BackButtonClicked : Event

        data object CountryButtonClick : Event

        data object CreateAccountClicked : Event
    }

    override fun reduce(
        previousState: State,
        event: Event
    ): Pair<State, Nothing?> {
        return when (event) {
            is Event.PhoneUpdated -> {
                previousState.copy(
                    phone = event.value,
                    phoneError = event.result.errorMessage
                ) to null
            }

            is Event.PasswordUpdated -> {
                previousState.copy(
                    password = event.value,
                    passwordError = event.result.errorMessage
                ) to null
            }

            is Event.TogglePasswordVisibility -> {
                previousState.copy(isPasswordVisible = !previousState.isPasswordVisible) to null
            }

            is Event.CountrySelected -> {
                previousState.copy(selectedCountry = event.country) to null
            }

            is Event.LoginClicked -> {
                previousState.copy(isLoading = true) to null
            }

            is Event.LoginFinished -> {
                previousState.copy(isLoading = false) to null
            }

            else -> previousState to null
        }
    }
}
