package com.example.clean.entry.core.domain.model

/**
 * A helper data class to encapsulate the result of a validation.
 * @property isSuccessful Whether the validation was successful.
 * @property errorMessage The error message if validation failed.
 */
data class ValidationResult(
    val isSuccessful: Boolean,
    val errorMessage: String? = null
)