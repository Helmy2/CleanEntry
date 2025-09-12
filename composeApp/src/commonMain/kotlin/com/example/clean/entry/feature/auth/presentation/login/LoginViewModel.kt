package com.example.clean.entry.feature.auth.presentation.login

import androidx.lifecycle.viewModelScope
import com.example.clean.entry.core.mvi.BaseViewModel
import com.example.clean.entry.feature.auth.domain.usecase.ValidatePasswordUseCase
import com.example.clean.entry.feature.auth.domain.usecase.ValidatePhoneUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(
    private val validatePhoneUseCase: ValidatePhoneUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : BaseViewModel<LoginReducer.State, LoginReducer.Event, LoginReducer.Effect>(
    reducer = LoginReducer,
    initialState = LoginReducer.State()
) {

    override fun handleEvent(event: LoginReducer.Event) {
        when (event) {
            is LoginReducer.Event.PhoneChanged -> {
                val regionCode = state.value.selectedCountryCode
                val result = validatePhoneUseCase(event.value, regionCode)
                setState(LoginReducer.Event.PhoneUpdated(event.value, result))
            }

            is LoginReducer.Event.PasswordChanged -> {
                val result = validatePasswordUseCase(event.value)
                setState(LoginReducer.Event.PasswordUpdated(event.value, result))
            }

            is LoginReducer.Event.LoginClicked -> {
                submitLogin()
            }

            else -> setState(event)
        }
    }

    private fun submitLogin() {
        setState(LoginReducer.Event.LoginClicked)
        viewModelScope.launch {
            // TODO: Implement login
            delay(2000)
            setState(LoginReducer.Event.LoginFinished)
        }
    }
}
