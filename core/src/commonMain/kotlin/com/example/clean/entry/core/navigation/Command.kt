package com.example.clean.entry.core.navigation

sealed class Command {
    data class NavigateTo(val destination: AppDestination) : Command()
    data object NavigateBack : Command()

    data class NavigateAsRoot(val destination: AppDestination) : Command()
}