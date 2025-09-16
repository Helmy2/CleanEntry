package com.example.clean.entry.core.navigation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AppNavigator {
    companion object Companion {
        object Keys {
            const val COUNTER_CODE = "COUNTER_CODE"
        }
    }

    val commands: StateFlow<Command>
    fun getValue(key: String): Flow<String>
    suspend fun setValue(key: String, value: String)
    fun onCommandConsumed()
    fun navigateAsRoot(destination: AppDestination)
    fun navigateBackWithResult(key: String, value: String)
    fun navigateBack()
    fun navigate(destination: AppDestination)
}