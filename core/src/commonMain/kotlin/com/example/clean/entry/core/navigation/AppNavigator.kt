package com.example.clean.entry.core.navigation

import kotlinx.coroutines.flow.Flow

interface AppNavigator {
    val commands: Flow<Command>

    fun navigateAsRoot(destination: AppDestination)

    fun navigateBack()

    fun navigate(destination: AppDestination)

    fun <T : NavigationSavedResult> getResultValue(key: String): Flow<T?>
    fun navigateBackWithResult(returnResult: NavigationSavedResult)
    val initialDestination: Flow<AppDestination>
}