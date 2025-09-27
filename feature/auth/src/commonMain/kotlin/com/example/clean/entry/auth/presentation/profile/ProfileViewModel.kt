package com.example.clean.entry.auth.presentation.profile

import androidx.lifecycle.viewModelScope
import com.example.clean.entry.auth.domain.repository.AuthRepository
import com.example.clean.entry.core.mvi.BaseViewModel
import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.core.navigation.AppNavigator
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val navigator: AppNavigator
) : BaseViewModel<ProfileReducer.State, ProfileReducer.Event, ProfileReducer.Effect>(
    reducer = ProfileReducer,
    initialState = ProfileReducer.State()
) {

    override fun handleEvent(event: ProfileReducer.Event) {
        setState(event)

        when (event) {
            is ProfileReducer.Event.OnLogoutClicked -> {
                performLogout()
            }

            is ProfileReducer.Event.LogoutSucceeded -> {
                navigator.navigateAsRoot(AppDestination.Login)
            }

            is ProfileReducer.Event.LogoutFailed -> {
            }
        }
    }

    private fun performLogout() {
        viewModelScope.launch {
            try {
                authRepository.clearAuthToken()
                handleEvent(ProfileReducer.Event.LogoutSucceeded)
            } catch (e: Exception) {
                handleEvent(
                    ProfileReducer.Event.LogoutFailed(
                        e.message ?: "An unexpected error occurred"
                    )
                )
            }
        }
    }
}