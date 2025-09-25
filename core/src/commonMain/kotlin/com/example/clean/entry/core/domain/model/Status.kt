package com.example.clean.entry.core.domain.model

/**
 * A sealed class representing the possible states of a data-loading operation.
 * This is a core domain model used to represent UI state across features.
 */
sealed class Status {
    object Idle : Status()
    object Loading : Status()
    class Error(val message: String) : Status()
}