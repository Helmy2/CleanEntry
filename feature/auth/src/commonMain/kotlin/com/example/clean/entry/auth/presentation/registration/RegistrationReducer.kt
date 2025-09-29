package com.example.clean.entry.auth.presentation.registration

import com.example.clean.entry.auth.domain.model.AuthMethod
import com.example.clean.entry.auth.domain.model.Country
import com.example.clean.entry.core.domain.model.ValidationResult
import com.example.clean.entry.core.mvi.Reducer

object RegistrationReducer :
    Reducer<RegistrationReducer.State, RegistrationReducer.Event, RegistrationReducer.Effect> {

    data class State(
        val email: String = "",
        val emailError: String? = null,
        val phone: String = "",
        val phoneError: String? = null,
        val password: String = "",
        val passwordError: String? = null,
        val confirmPassword: String = "",
        val confirmPasswordError: String? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val selectedCountry: Country = Country.Egypt,
        val authMethod: AuthMethod = AuthMethod.PHONE,
        val verificationId: String? = null,
        val otp: String = "",
        val otpCount: Int = 6
    ) : Reducer.ViewState {
        val isContinueButtonEnabled
            get() = when {
                verificationId != null -> otp.length == otpCount && !isLoading
                authMethod == AuthMethod.EMAIL -> email.isNotBlank() && emailError == null &&
                        password.isNotBlank() && passwordError == null &&
                        confirmPassword.isNotBlank() && confirmPasswordError == null && !isLoading

                authMethod == AuthMethod.PHONE -> phone.isNotBlank() && phoneError == null && !isLoading
                else -> false
            }
    }

    sealed interface Event : Reducer.ViewEvent {
        data class EmailChanged(val value: String) : Event
        data class PhoneChanged(val value: String) : Event
        data class PasswordChanged(val value: String) : Event
        data class ConfirmPasswordChanged(val value: String) : Event
        data class OtpChanged(val value: String) : Event
        data object Submit : Event
        data class CountrySelected(val country: Country) : Event
        data class AuthMethodChanged(val method: AuthMethod) : Event

        data class EmailUpdated(val value: String, val result: ValidationResult) : Event
        data class PhoneUpdated(val value: String, val result: ValidationResult) : Event
        data class PasswordUpdated(val value: String, val result: ValidationResult) : Event
        data class ConfirmPasswordUpdated(val value: String, val result: ValidationResult) : Event
        data class VerificationCodeSent(val verificationId: String?) : Event

        data object RegistrationSuccess : Event
        data class RegistrationFailed(val error: String) : Event

        data object BackButtonClicked : Event
        data object CountryButtonClick : Event
    }

    sealed interface Effect : Reducer.ViewEffect

    override fun reduce(previousState: State, event: Event): Pair<State, Nothing?> {
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

            is Event.ConfirmPasswordUpdated -> previousState.copy(
                confirmPassword = event.value,
                confirmPasswordError = event.result.errorMessage
            ) to null

            is Event.CountrySelected -> previousState.copy(selectedCountry = event.country) to null
            is Event.Submit -> previousState.copy(isLoading = true, error = null) to null
            is Event.RegistrationSuccess -> State() to null
            is Event.RegistrationFailed -> previousState.copy(
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