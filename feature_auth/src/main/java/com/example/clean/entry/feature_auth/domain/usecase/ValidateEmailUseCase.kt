package com.example.clean.entry.feature_auth.domain.usecase

import com.example.clean.entry.core.domain.model.StringResource
import com.example.clean.entry.core.domain.model.ValidationResult


/**
 * A use case that validates a user's email address.
 * According to business rules, the email must not be empty and must be in a valid format.
 */
class ValidateEmailUseCase {

    private val emailRegex = Regex(
        """[a-zA-Z0-9+._%\-]{1,256}@[a-zA-Z0-9][a-zA-Z0-9\-]{0,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{0,25})+"""
    )

    /**
     * Executes the use case.
     * @param email The email to validate.
     * @return A ValidationResult which is either a success or an error with a message.
     */
    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = StringResource.FromString("Email cannot be empty.")
            )
        }
        if (!emailRegex.matches(email)) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = StringResource.FromString("That's not a valid email.")
            )
        }
        return ValidationResult(isSuccessful = true)
    }
}
