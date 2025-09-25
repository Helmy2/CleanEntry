package com.example.clean.entry.auth.presentation.registration

import androidx.lifecycle.viewModelScope
import com.example.clean.entry.auth.domain.model.AuthMethod
import com.example.clean.entry.auth.domain.repository.AuthRepository
import com.example.clean.entry.auth.domain.usecase.ValidateConfirmPasswordUseCase
import com.example.clean.entry.auth.domain.usecase.ValidateEmailUseCase
import com.example.clean.entry.auth.domain.usecase.ValidatePasswordUseCase
import com.example.clean.entry.auth.domain.usecase.ValidatePhoneUseCase
import com.example.clean.entry.auth.navigation.CounterCodeResult
import com.example.clean.entry.auth.util.getReadableFirebaseAuthErrorMessage
import com.example.clean.entry.core.mvi.BaseViewModel
import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.core.navigation.AppNavigator
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePhoneUseCase: ValidatePhoneUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase,
    private val authRepository: AuthRepository,
    private val navigator: AppNavigator,
) : BaseViewModel<RegistrationReducer.State, RegistrationReducer.Event, RegistrationReducer.Effect>(
    reducer = RegistrationReducer,
    initialState = RegistrationReducer.State()
) {
    override suspend fun initialDataLoad() {
        super.initialDataLoad()
        viewModelScope.launch {
            navigator.getResultValue<CounterCodeResult>(CounterCodeResult.KEY)
                .collect {
                    if (it != null) {
                        handleEvent(RegistrationReducer.Event.CountrySelected(it.country))
                    }
                }
        }
    }

    override fun handleEvent(event: RegistrationReducer.Event) {
        when (event) {
            is RegistrationReducer.Event.EmailChanged -> {
                val result = validateEmailUseCase(event.value)
                setState(RegistrationReducer.Event.EmailUpdated(event.value, result))
            }

            is RegistrationReducer.Event.PhoneChanged -> {
                val regionCode = state.value.selectedCountry.code
                val result = validatePhoneUseCase(event.value, regionCode)
                setState(RegistrationReducer.Event.PhoneUpdated(event.value, result))
            }

            is RegistrationReducer.Event.PasswordChanged -> {
                val result = validatePasswordUseCase(event.value)
                setState(RegistrationReducer.Event.PasswordUpdated(event.value, result))
            }

            is RegistrationReducer.Event.ConfirmPasswordChanged -> {
                val result = validateConfirmPasswordUseCase(state.value.password, event.value)
                setState(RegistrationReducer.Event.ConfirmPasswordUpdated(event.value, result))
            }

            is RegistrationReducer.Event.Submit -> {
                submitRegistration()
                setState(event)
            }

            is RegistrationReducer.Event.BackButtonClicked -> {
                navigator.navigateBack()
            }

            is RegistrationReducer.Event.CountryButtonClick -> {
                navigator.navigate(
                    AppDestination.CountryCodePicker(state.value.selectedCountry.code)
                )
            }

            is RegistrationReducer.Event.RegistrationSuccess -> {
                navigator.navigateAsRoot(AppDestination.Dashboard)
            }

            else -> setState(event)
        }
    }

    private fun submitRegistration() {
        viewModelScope.launch {
            val state = state.value
            when (state.authMethod) {
                AuthMethod.EMAIL_PASSWORD -> {
                    authRepository.registerWithEmailAndPassword(state.email, state.password)
                        .onSuccess {
                            handleEvent(RegistrationReducer.Event.RegistrationSuccess)
                        }
                        .onFailure {
                            val errorMessage = getReadableFirebaseAuthErrorMessage(it.message ?: "")
                            handleEvent(RegistrationReducer.Event.RegistrationFailed(errorMessage))
                        }
                }

                AuthMethod.PHONE -> {
                    if (state.verificationId == null) {
                        val fullPhoneNumber = "${state.selectedCountry.dialCode}${state.phone}"
                        authRepository.sendVerificationCode(fullPhoneNumber)
                            .onSuccess {
                                handleEvent(RegistrationReducer.Event.VerificationCodeSent(it))
                            }
                            .onFailure {
                                val errorMessage =
                                    getReadableFirebaseAuthErrorMessage(it.message ?: "")
                                handleEvent(
                                    RegistrationReducer.Event.RegistrationFailed(
                                        errorMessage
                                    )
                                )
                            }
                    } else {
                        authRepository.signInWithPhoneNumber(state.verificationId, state.otp)
                            .onSuccess {
                                handleEvent(RegistrationReducer.Event.RegistrationSuccess)
                            }
                            .onFailure {
                                val errorMessage =
                                    getReadableFirebaseAuthErrorMessage(it.message ?: "")
                                handleEvent(
                                    RegistrationReducer.Event.RegistrationFailed(
                                        errorMessage
                                    )
                                )
                            }
                    }
                }
            }
        }
    }
}
