package com.example.clean.entry.auth.presentation.login

import androidx.lifecycle.viewModelScope
import com.example.clean.entry.auth.domain.model.AuthMethod
import com.example.clean.entry.auth.domain.repository.AuthRepository
import com.example.clean.entry.auth.domain.usecase.ValidateEmailUseCase
import com.example.clean.entry.auth.domain.usecase.ValidatePasswordUseCase
import com.example.clean.entry.auth.domain.usecase.ValidatePhoneUseCase
import com.example.clean.entry.auth.navigation.CounterCodeResult
import com.example.clean.entry.auth.util.getReadableFirebaseAuthErrorMessage
import com.example.clean.entry.core.mvi.BaseViewModel
import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.core.navigation.AppNavigator
import kotlinx.coroutines.launch

class LoginViewModel(
    private val validatePhoneUseCase: ValidatePhoneUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val authRepository: AuthRepository,
    private val navigator: AppNavigator,
) : BaseViewModel<LoginReducer.State, LoginReducer.Event, LoginReducer.Effect>(
    reducer = LoginReducer,
    initialState = LoginReducer.State()
) {
    override suspend fun initialDataLoad() {
        super.initialDataLoad()
        viewModelScope.launch {
            navigator.getResultValue<CounterCodeResult>(CounterCodeResult.KEY)
                .collect {
                    if (it != null) {
                        handleEvent(LoginReducer.Event.CountrySelected(it.country))
                    }
                }
        }
        viewModelScope.launch {
            authRepository.isAuthenticated.collect {
                if (it) {
                    navigator.navigateAsRoot(AppDestination.Feed)
                }
            }
        }
    }

    override fun handleEvent(event: LoginReducer.Event) {
        when (event) {
            is LoginReducer.Event.EmailChanged -> {
                val result = validateEmailUseCase(event.value)
                setState(LoginReducer.Event.EmailUpdated(event.value, result))
            }

            is LoginReducer.Event.PhoneChanged -> {
                val regionCode = state.value.selectedCountry.code
                val result = validatePhoneUseCase(event.value, regionCode)
                setState(LoginReducer.Event.PhoneUpdated(event.value, result))
            }

            is LoginReducer.Event.PasswordChanged -> {
                val result = validatePasswordUseCase(event.value)
                setState(LoginReducer.Event.PasswordUpdated(event.value, result))
            }

            is LoginReducer.Event.LoginClicked -> {
                submitLogin()
                setState(event)
            }

            is LoginReducer.Event.BackButtonClicked -> {
                navigator.navigateBack()
            }

            is LoginReducer.Event.CreateAccountClicked -> {
                navigator.navigate(AppDestination.Registration)
            }

            is LoginReducer.Event.CountryButtonClick -> {
                navigator.navigate(
                    AppDestination.CountryCodePicker(state.value.selectedCountry.code)
                )
            }

            else -> setState(event)
        }
    }

    private fun submitLogin() {
        viewModelScope.launch {
            val state = state.value
            when (state.authMethod) {
                AuthMethod.EMAIL_PASSWORD -> {
                    authRepository.loginWithEmailAndPassword(state.email, state.password)
                        .onSuccess {
                            handleEvent(LoginReducer.Event.LoginSuccess)
                        }
                        .onFailure {
                            val errorMessage = getReadableFirebaseAuthErrorMessage(it.message ?: "")
                            handleEvent(LoginReducer.Event.LoginFailed(errorMessage))
                        }
                }

                AuthMethod.PHONE -> {
                    if (state.verificationId == null) {
                        val fullPhoneNumber = "${state.selectedCountry.dialCode}${state.phone}"
                        authRepository.sendVerificationCode(fullPhoneNumber)
                            .onSuccess {
                                handleEvent(LoginReducer.Event.VerificationCodeSent(it))
                            }
                            .onFailure {
                                val errorMessage =
                                    getReadableFirebaseAuthErrorMessage(it.message ?: "")
                                handleEvent(LoginReducer.Event.LoginFailed(errorMessage))
                            }
                    } else {
                        authRepository.signInWithPhoneNumber(state.verificationId, state.otp)
                            .onSuccess {
                                handleEvent(LoginReducer.Event.LoginSuccess)
                            }
                            .onFailure {
                                val errorMessage =
                                    getReadableFirebaseAuthErrorMessage(it.message ?: "")
                                handleEvent(LoginReducer.Event.LoginFailed(errorMessage))
                            }
                    }
                }
            }
        }
    }
}
