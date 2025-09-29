package com.example.clean.entry.auth.domain.usecase

import com.example.clean.entry.core.domain.model.ValidationResult


/**
 * A use case that validates a user's first name.
 * According to business rules, the first name must not be empty and must be at least 2 characters long.
 */
class ValidateFirstNameUseCase {

    /**
     * Executes the use case.
     * @param firstName The first name to validate.
     * @return A ValidationResult which is either a success or an error with a message.
     */
    operator fun invoke(firstName: String): ValidationResult {
        if (firstName.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Name cannot be empty."
            )
        }
        if (firstName.length < 2) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Name must be at least 2 characters long."
            )
        }
        return ValidationResult(isSuccessful = true)
    }
}

