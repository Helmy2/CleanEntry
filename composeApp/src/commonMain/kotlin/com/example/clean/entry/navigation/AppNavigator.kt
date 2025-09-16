package com.example.clean.entry.navigation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map


class AppNavigator(val startDestination: Any) {

    val saveState = MutableStateFlow<List<Pair<String, String>>>(emptyList())

    companion object {
        object Keys {
            const val COUNTER_CODE = "COUNTER_CODE"
        }
    }

    private val _commands = MutableStateFlow<Command>(Command.Idle)
    val commands: StateFlow<Command> = _commands

    fun navigate(destination: AppDestination) {
        _commands.value = Command.NavigateTo(destination)
    }

    fun navigateBack() {
        _commands.value = Command.NavigateBack
    }

    fun navigateBackWithResult(key: String, value: String) {
        _commands.value = Command.NavigateBackWithResult(key, value)
    }

    fun navigateAsRoot(destination: AppDestination) {
        _commands.value = Command.NavigateAsRoot(destination)
    }

    fun onCommandConsumed() {
        _commands.value = Command.Idle
    }

    suspend fun setValue(key: String, value: String) {
        val currentList = saveState.value.toMutableList()
        val index = currentList.indexOfFirst { it.first == key }
        if (index != -1) {
            currentList[index] = key to value
        } else {
            currentList.add(key to value)
        }
        saveState.emit(currentList)
    }

    fun getValue(key: String): Flow<String> {
        return saveState.filter { it.any { it.first == key } }
            .map { it.first { it.first == key }.second }
    }
}

