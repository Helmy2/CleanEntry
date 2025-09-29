package com.example.clean.entry.navigation

import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.core.navigation.AppNavigator
import com.example.clean.entry.core.navigation.Command
import com.example.clean.entry.core.navigation.NavigationSavedResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AppNavigatorImpl(
    private val scope: CoroutineScope
) : AppNavigator {

    private val savedResults = MutableStateFlow<Map<String, NavigationSavedResult>>(
        emptyMap()
    )

    private val _commands = Channel<Command>(Channel.BUFFERED)

    override val commands = _commands.receiveAsFlow()

    override fun navigate(destination: AppDestination) {
        scope.launch {
            _commands.send(Command.NavigateTo(destination))
        }
    }

    override fun navigateBack() {
        scope.launch {
            _commands.send(Command.NavigateBack)
        }
    }

    override fun navigateBackWithResult(returnResult: NavigationSavedResult) {
        savedResults.update { it + (returnResult.key to returnResult) }
        navigateBack()
    }

    override fun navigateAsRoot(destination: AppDestination) {
        scope.launch {
            _commands.send(Command.NavigateAsRoot(destination))
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : NavigationSavedResult> getResultValue(key: String): Flow<T?> {
        return savedResults.map { it[key] as? T }.distinctUntilChanged()
    }
}


