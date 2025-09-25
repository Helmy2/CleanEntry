package com.example.clean.entry.auth.domain.usecase

import com.example.clean.entry.core.domain.model.ValidationResult

class ValidateConfirmPasswordUseCase {
    operator fun invoke(password: String, confirmPassword: String): ValidationResult {
        if (password != confirmPassword) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Passwords do not match" // TODO: Use string resources
            )
        }
        return ValidationResult(isSuccessful = true)
    }
}
