package com.example.clean.entry.feature.auth.domain.usecase

import com.example.clean.entry.core.domain.model.ValidationResult
import com.example.clean.entry.core.domain.model.StringResource

/**
 * A use case that validates a user's surname.
 * According to business rules, the surname must not be empty and must be at least 2 characters long.
 */
class ValidateSurnameUseCase {

    /**
     * Executes the use case.
     * @param surname The surname to validate.
     * @return A ValidationResult which is either a success or an error with a message.
     */
    operator fun invoke(surname: String): ValidationResult {
        if (surname.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = StringResource.FromString("Surname cannot be empty.")
            )
        }
        if (surname.length < 2) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = StringResource.FromString("Surname must be at least 2 characters long.")
            )
        }
        return ValidationResult(isSuccessful = true)
    }
}