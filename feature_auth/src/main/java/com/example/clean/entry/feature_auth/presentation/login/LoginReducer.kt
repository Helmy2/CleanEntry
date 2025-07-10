package com.example.clean.entry.feature_auth.presentation.login

import com.example.clean.core.mvi.Reducer
import com.example.clean.core.util.StringResource
import com.example.clean.entry.feature_auth.domain.model.ValidationResult

/**
 * Defines the contract for the Login screen and also acts as the Reducer
 * for its state transformations.
 */
object LoginReducer : Reducer<LoginReducer.State, LoginReducer.Event, LoginReducer.Effect> {

    data class State(
        val phone: String = "",
        val phoneError: StringResource? = null,
        val password: String = "",
        val passwordError: StringResource? = null,
        val isPasswordVisible: Boolean = false,
        val isLoading: Boolean = false,
        val isLoginButtonEnabled: Boolean = false,
        val selectedCountryCode: String = "EG", // Default to Egypt
        val selectedCountryDialCode: String = "+20"
    ) : Reducer.ViewState

    sealed interface Event : Reducer.ViewEvent {
        // UI Events
        data class PhoneChanged(val value: String) : Event
        data class PasswordChanged(val value: String) : Event
        data object TogglePasswordVisibility : Event
        data object LoginClicked : Event

        // Internal Events
        data class PhoneUpdated(val value: String, val result: ValidationResult) : Event
        data class PasswordUpdated(val value: String, val result: ValidationResult) : Event
        data object LoginFinished : Event
    }

    sealed interface Effect : Reducer.ViewEffect {
        data object LoginSuccess : Effect
    }

    override fun reduce(
        previousState: State,
        event: Event
    ): Pair<State, Effect?> {
        val newState = when (event) {
            is Event.PhoneUpdated -> {
                previousState.copy(phone = event.value, phoneError = event.result.errorMessage)
            }
            is Event.PasswordUpdated -> {
                previousState.copy(password = event.value, passwordError = event.result.errorMessage)
            }
            is Event.TogglePasswordVisibility -> {
                previousState.copy(isPasswordVisible = !previousState.isPasswordVisible)
            }
            is Event.LoginClicked -> {
                previousState.copy(isLoading = true)
            }
            is Event.LoginFinished -> {
                previousState.copy(isLoading = false)
            }
            else -> previousState
        }

        val isFormValid = newState.phone.isNotBlank() && newState.phoneError == null &&
                newState.password.isNotBlank() && newState.passwordError == null

        return newState.copy(isLoginButtonEnabled = isFormValid) to null
    }
}
