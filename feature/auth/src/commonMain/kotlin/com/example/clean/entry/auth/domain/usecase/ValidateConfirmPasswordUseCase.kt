package com.example.clean.entry.auth.domain.usecase

import com.example.clean.entry.core.domain.model.ValidationResult

class ValidateConfirmPasswordUseCaseImpl : ValidateConfirmPasswordUseCase {
    override operator fun invoke(password: String, confirmPassword: String): ValidationResult {
        if (password != confirmPassword) {
            return ValidationResult(
                isSuccessful = false, errorMessage = "Passwords do not match"
            )
        }
        return ValidationResult(isSuccessful = true)
    }
}


/**
 * Use case for validating that the confirmation password matches the original password.
 */
interface ValidateConfirmPasswordUseCase {

    /**
     * Validates whether the provided password and confirmation password match.
     *
     * This use case can be invoked as a function.
     *
     * @param password The original password entered by the user.
     * @param confirmPassword The confirmation password entered by the user.
     * @return [ValidationResult] indicating success if the passwords match, or failure with an error message if they don't.
     */
    operator fun invoke(password: String, confirmPassword: String): ValidationResult
}