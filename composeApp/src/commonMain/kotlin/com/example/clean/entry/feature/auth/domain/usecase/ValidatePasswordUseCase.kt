package com.example.clean.entry.feature.auth.domain.usecase

import com.example.clean.entry.core.domain.model.ValidationResult
import com.example.clean.entry.core.domain.model.StringResource


/**
 * A use case that validates a user's password.
 * According to business rules, the password must be at least 6 characters long.
 */
class ValidatePasswordUseCase {

    /**
     * Executes the use case.
     * @param password The password to validate.
     * @return A ValidationResult which is either a success or an error with a message.
     */
    operator fun invoke(password: String): ValidationResult {
        if (password.length < 6) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = StringResource.FromString("Password must be at least 6 characters long.")
            )
        }
        return ValidationResult(isSuccessful = true)
    }
}