package com.example.clean.feature_auth.domain.model

import com.example.clean.core.util.StringResource

/**
 * A helper data class to encapsulate the result of a validation.
 * @property isSuccessful Whether the validation was successful.
 * @property errorMessage The error message if validation failed.
 */
data class ValidationResult(
    val isSuccessful: Boolean,
    val errorMessage: StringResource? = null
)