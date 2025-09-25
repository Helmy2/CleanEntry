package com.example.clean.entry.auth.domain.usecase

import com.example.clean.entry.core.domain.model.ValidationResult
import com.example.clean.entry.core.util.PhoneNumberVerifier

/**
 * A use case that validates a phone number using Google's libphonenumber.
 *
 * @param verifier An instance of PhoneNumberUtil, which should be provided by Koin.
 */
class ValidatePhoneUseCase(private val verifier: PhoneNumberVerifier) {

    /**
     * Executes the use case.
     * @param phone The phone number to validate.
     * @param regionCode The ISO 3166-1 alpha-2 country code (e.g., "EG", "US").
     * @return A ValidationResult which is either a success or an error with a message.
     */
    operator fun invoke(phone: String, regionCode: String): ValidationResult {
        if (phone.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Phone number cannot be empty."
            )
        }
        return try {
            val isValid = verifier.isValidNumber(phone, regionCode)
            if (isValid) {
                ValidationResult(isSuccessful = true)
            } else {
                ValidationResult(
                    isSuccessful = false,
                    errorMessage = "Invalid phone number for the selected region."
                )
            }
        } catch (_: Exception) {
            ValidationResult(
                isSuccessful = false,
                errorMessage = "Invalid phone number format."
            )
        }
    }
}