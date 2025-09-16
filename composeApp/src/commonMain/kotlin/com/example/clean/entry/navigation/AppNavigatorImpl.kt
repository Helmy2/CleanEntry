package com.example.clean.entry.navigation

import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.core.navigation.AppNavigator
import com.example.clean.entry.core.navigation.Command
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map


class AppNavigatorImpl(val startDestination: AppDestination) : AppNavigator {

    val saveState = MutableStateFlow<List<Pair<String, String>>>(emptyList())

    private val _commands = MutableStateFlow<Command>(Command.Idle)
    override val commands: StateFlow<Command> = _commands

    override fun navigate(destination: AppDestination) {
        _commands.value = Command.NavigateTo(destination)
    }

    override fun navigateBack() {
        _commands.value = Command.NavigateBack
    }

    override fun navigateBackWithResult(key: String, value: String) {
        _commands.value = Command.NavigateBackWithResult(key, value)
    }

    override fun navigateAsRoot(destination: AppDestination) {
        _commands.value = Command.NavigateAsRoot(destination)
    }

    override fun onCommandConsumed() {
        _commands.value = Command.Idle
    }

    override suspend fun setValue(key: String, value: String) {
        val currentList = saveState.value.toMutableList()
        val index = currentList.indexOfFirst { it.first == key }
        if (index != -1) {
            currentList[index] = key to value
        } else {
            currentList.add(key to value)
        }
        saveState.emit(currentList)
    }

    override fun getValue(key: String): Flow<String> {
        return saveState.filter { it.any { it.first == key } }
            .map { it.first { it.first == key }.second }
    }
}


