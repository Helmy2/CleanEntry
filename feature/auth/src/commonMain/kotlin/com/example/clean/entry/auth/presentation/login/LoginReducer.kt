package com.example.clean.entry.auth.presentation.login

import com.example.clean.entry.auth.domain.model.AuthMethod
import com.example.clean.entry.auth.domain.model.Country
import com.example.clean.entry.core.domain.model.ValidationResult
import com.example.clean.entry.core.mvi.Reducer

object LoginReducer : Reducer<LoginReducer.State, LoginReducer.Event, LoginReducer.Effect> {

    data class State(
        val email: String = "",
        val emailError: String? = null,
        val phone: String = "",
        val phoneError: String? = null,
        val password: String = "",
        val passwordError: String? = null,
        val isPasswordVisible: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null,
        val selectedCountry: Country = Country.Egypt,
        val authMethod: AuthMethod = AuthMethod.EMAIL_PASSWORD,
        val verificationId: String? = null,
        val otp: String = "",
        val otpCount: Int = 6
    ) : Reducer.ViewState {
        val isLoginButtonEnabled
            get() = when {
                verificationId != null -> otp.length == otpCount && !isLoading
                authMethod == AuthMethod.EMAIL_PASSWORD -> email.isNotBlank() && emailError == null &&
                        password.isNotBlank() && passwordError == null && !isLoading

                authMethod == AuthMethod.PHONE -> phone.isNotBlank() && phoneError == null && !isLoading
                else -> false
            }
    }

    sealed interface Event : Reducer.ViewEvent {
        data class EmailChanged(val value: String) : Event
        data class PhoneChanged(val value: String) : Event
        data class PasswordChanged(val value: String) : Event
        data class OtpChanged(val value: String) : Event
        data object TogglePasswordVisibility : Event
        data object LoginClicked : Event
        data class CountrySelected(val country: Country) : Event
        data class AuthMethodChanged(val method: AuthMethod) : Event

        data class EmailUpdated(val value: String, val result: ValidationResult) : Event
        data class PhoneUpdated(val value: String, val result: ValidationResult) : Event
        data class PasswordUpdated(val value: String, val result: ValidationResult) : Event
        data class VerificationCodeSent(val verificationId: String) : Event
        data object LoginSuccess : Event
        data class LoginFailed(val error: String) : Event

        data object BackButtonClicked : Event
        data object CountryButtonClick : Event
        data object CreateAccountClicked : Event
    }

    sealed interface Effect : Reducer.ViewEffect

    override fun reduce(
        previousState: State,
        event: Event
    ): Pair<State, Nothing?> {
        return when (event) {
            is Event.EmailUpdated -> previousState.copy(
                email = event.value,
                emailError = event.result.errorMessage
            ) to null

            is Event.PhoneUpdated -> previousState.copy(
                phone = event.value,
                phoneError = event.result.errorMessage
            ) to null

            is Event.PasswordUpdated -> previousState.copy(
                password = event.value,
                passwordError = event.result.errorMessage
            ) to null

            is Event.TogglePasswordVisibility -> previousState.copy(isPasswordVisible = !previousState.isPasswordVisible) to null
            is Event.CountrySelected -> previousState.copy(selectedCountry = event.country) to null
            is Event.LoginClicked -> previousState.copy(isLoading = true, error = null) to null
            is Event.LoginSuccess -> previousState.copy(isLoading = false) to null
            is Event.LoginFailed -> previousState.copy(
                isLoading = false,
                error = event.error,
                otp = ""
            ) to null

            is Event.AuthMethodChanged -> previousState.copy(
                authMethod = event.method,
                error = null
            ) to null

            is Event.VerificationCodeSent -> previousState.copy(
                isLoading = false,
                verificationId = event.verificationId
            ) to null

            is Event.OtpChanged -> previousState.copy(otp = event.value) to null
            else -> previousState to null
        }
    }
}