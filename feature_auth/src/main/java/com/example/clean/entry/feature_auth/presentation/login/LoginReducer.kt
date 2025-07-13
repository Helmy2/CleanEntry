package com.example.clean.entry.feature_auth.presentation.login

import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.domain.model.ValidationResult
import com.example.clean.entry.mvi.Reducer
import com.example.clean.entry.util.StringResource

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
        val selectedCountryCode: String = "EG",
        val selectedCountryDialCode: String = "+20",
        val selectedCountryFlag: String = "🇪🇬"
    ) : Reducer.ViewState {
        val isLoginButtonEnabled
            get() = phone.isNotBlank() && phoneError == null &&
                    password.isNotBlank() && passwordError == null
    }

    sealed interface Event : Reducer.ViewEvent {
        // UI Events
        data class PhoneChanged(val value: String) : Event
        data class PasswordChanged(val value: String) : Event
        data object TogglePasswordVisibility : Event
        data object LoginClicked : Event
        data class CountrySelected(val result: Country) : Event // New event

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
                previousState.copy(
                    selectedCountryCode = event.result.code,
                    selectedCountryDialCode = event.result.dialCode,
                    selectedCountryFlag = event.result.flagEmoji
                ) to null
            }

            is Event.LoginClicked -> {
                previousState.copy(isLoading = true) to null
            }

            is Event.LoginFinished -> {
                previousState.copy(isLoading = false) to Effect.LoginSuccess
            }

            else -> previousState to null
        }
    }
}
