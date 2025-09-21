package com.example.clean.entry.auth.presentation.login

import androidx.lifecycle.viewModelScope
import com.example.clean.entry.auth.domain.usecase.ValidatePasswordUseCase
import com.example.clean.entry.auth.domain.usecase.ValidatePhoneUseCase
import com.example.clean.entry.auth.navigation.CounterCodeResult
import com.example.clean.entry.core.mvi.BaseViewModel
import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.core.navigation.AppNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(
    private val validatePhoneUseCase: ValidatePhoneUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
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
    }

    override fun handleEvent(event: LoginReducer.Event) {
        when (event) {
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

            is LoginReducer.Event.LoginFinished -> {
                navigator.navigateAsRoot(AppDestination.Dashboard)
            }

            else -> setState(event)
        }
    }

    private fun submitLogin() {
        viewModelScope.launch {
            // TODO: Implement login
            delay(2000)
            handleEvent(LoginReducer.Event.LoginFinished)
        }
    }
}
