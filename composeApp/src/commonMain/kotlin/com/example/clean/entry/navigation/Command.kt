package com.example.clean.entry.navigation

sealed class Command {
    class NavigateTo(val destination: AppDestination) : Command()
    object NavigateBack : Command()
    class NavigateAsRoot(val destination: AppDestination) : Command()
}