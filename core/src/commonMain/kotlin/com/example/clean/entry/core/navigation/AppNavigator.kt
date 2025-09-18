package com.example.clean.entry.core.navigation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AppNavigator {
    val startDestination: AppDestination
    val commands: StateFlow<Command>
    fun onCommandConsumed()

    fun navigateAsRoot(destination: AppDestination)

    fun navigateBack()

    fun navigate(destination: AppDestination)

    fun <T : NavigationSavedResult> getResultValue(key: String): Flow<T?>
    fun navigateBackWithResult(returnResult: NavigationSavedResult)
}