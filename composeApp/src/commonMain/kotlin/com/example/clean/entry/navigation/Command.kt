package com.example.clean.entry.navigation

sealed class Command {
    data object Idle : Command()
    data class NavigateTo(val destination: AppDestination) : Command()
    data object NavigateBack : Command()

    data class NavigateBackWithResult(val key : String, val value: String) : Command()
    data class NavigateAsRoot(val destination: AppDestination) : Command()
}