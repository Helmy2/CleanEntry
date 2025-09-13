package com.example.clean.entry.navigation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf


class AppNavigator(val startDestination: Any) {
    private val _commands = mutableStateOf<Command?>(null)
    val commands: State<Command?> = _commands

    fun navigate(destination: AppDestination) {
        _commands.value = Command.NavigateTo(destination)
    }

    fun navigateBack() {
        _commands.value = Command.NavigateBack
    }

    fun navigateAsRoot(destination: AppDestination) {
        _commands.value = Command.NavigateAsRoot(destination)
    }

    fun onCommandConsumed() {
        _commands.value = null
    }
}

