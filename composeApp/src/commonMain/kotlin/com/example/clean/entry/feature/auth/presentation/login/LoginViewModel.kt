package com.example.clean.entry.feature.auth.presentation.login

import androidx.lifecycle.viewModelScope
import com.example.clean.entry.core.mvi.BaseViewModel
import com.example.clean.entry.feature.auth.domain.model.Country
import com.example.clean.entry.feature.auth.domain.repository.CountryRepository
import com.example.clean.entry.feature.auth.domain.usecase.ValidatePasswordUseCase
import com.example.clean.entry.feature.auth.domain.usecase.ValidatePhoneUseCase
import com.example.clean.entry.navigation.AppDestination
import com.example.clean.entry.navigation.AppNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(
    private val validatePhoneUseCase: ValidatePhoneUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val countryRepository: CountryRepository,
    private val navigator: AppNavigator,
) : BaseViewModel<LoginReducer.State, LoginReducer.Event, Nothing>(
    reducer = LoginReducer,
    initialState = LoginReducer.State()
) {
    override suspend fun initialDataLoad() {
        super.initialDataLoad()
        viewModelScope.launch {
            navigator.getValue(AppNavigator.Companion.Keys.COUNTER_CODE).collect {
                val country = countryRepository.getCountry(it).getOrNull() ?: Country.Egypt
                handleEvent(LoginReducer.Event.CountrySelected(country))
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
            }

            is LoginReducer.Event.BackButtonClicked -> {
                navigator.navigateBack()
            }

            is LoginReducer.Event.CreateAccountClicked -> {
                navigator.navigate(AppDestination.Auth.Registration)
            }

            is LoginReducer.Event.CountryButtonClick -> {
                navigator.navigate(
                    AppDestination.Auth.CountryCodePicker(state.value.selectedCountry.code)
                )
            }

            is LoginReducer.Event.LoginFinished -> {
                navigator.navigateAsRoot(AppDestination.Dashboard)
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
