package com.example.clean.entry.auth.presentation.profile

import com.example.clean.entry.core.mvi.Reducer

object ProfileReducer : Reducer<ProfileReducer.State, ProfileReducer.Event, ProfileReducer.Effect> {

    data class State(
        val isLoading: Boolean = false,
        val error: String? = null
    ) : Reducer.ViewState

    sealed interface Event : Reducer.ViewEvent {
        data object OnLogoutClicked : Event
        data object LogoutSucceeded : Event
        data class LogoutFailed(val errorMessage: String) : Event
    }

    sealed interface Effect : Reducer.ViewEffect

    override fun reduce(previousState: State, event: Event): Pair<State, Effect?> {
        return when (event) {
            is Event.OnLogoutClicked -> {
                previousState.copy(isLoading = true, error = null) to null
            }

            is Event.LogoutSucceeded -> {
                previousState.copy(isLoading = false, error = null) to null
            }

            is Event.LogoutFailed -> {
                previousState.copy(isLoading = false, error = event.errorMessage) to null
            }
        }
    }
}