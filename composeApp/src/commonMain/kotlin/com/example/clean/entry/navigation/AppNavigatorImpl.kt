package com.example.clean.entry.navigation

import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.core.navigation.AppNavigator
import com.example.clean.entry.core.navigation.Command
import com.example.clean.entry.core.navigation.NavigationSavedResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map


class AppNavigatorImpl(override val startDestination: AppDestination) : AppNavigator {

    private val saveSatedChannel = MutableStateFlow<Map<String, NavigationSavedResult>>(
        emptyMap()
    )

    private val _commands = MutableStateFlow<Command>(Command.Idle)

    override val commands: StateFlow<Command> = _commands

    override fun navigate(destination: AppDestination) {
        _commands.value = Command.NavigateTo(destination)
    }

    override fun navigateBack() {
        _commands.value = Command.NavigateBack
    }

    override fun navigateBackWithResult(returnResult: NavigationSavedResult) {
        val returnResultMap = buildMap {
            putAll(saveSatedChannel.value)
            put(returnResult.key, returnResult)
        }
        saveSatedChannel.tryEmit(returnResultMap)
        navigateBack()
    }

    override fun navigateAsRoot(destination: AppDestination) {
        _commands.value = Command.NavigateAsRoot(destination)
    }

    override fun onCommandConsumed() {
        _commands.value = Command.Idle
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : NavigationSavedResult> getResultValue(key: String): Flow<T?> {
        return saveSatedChannel.map {
            try {
                it.getOrElse(key) { null } as? T
            } catch (_: Exception) {
                null
            }
        }
    }
}


