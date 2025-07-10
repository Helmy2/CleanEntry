package com.example.clean.feature_auth.domain.usecase

import android.util.Patterns
import com.example.clean.core.util.StringResource
import com.example.clean.feature_auth.domain.model.ValidationResult

/**
 * A use case that validates a user's email address.
 * According to business rules, the email must not be empty and must be in a valid format.
 */
class ValidateEmailUseCase {

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
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = StringResource.FromString("That's not a valid email.")
            )
        }
        return ValidationResult(isSuccessful = true)
    }
}
