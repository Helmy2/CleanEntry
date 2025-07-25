package com.example.clean.entry.feature_auth.domain.usecase

import com.example.clean.entry.core.domain.model.ValidationResult
import com.example.clean.entry.core.domain.model.StringResource
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber


/**
 * A use case that validates a phone number using Google's libphonenumber.
 *
 * @param phoneNumberUtil An instance of PhoneNumberUtil, which should be provided by Koin.
 */
class ValidatePhoneUseCase(private val phoneNumberUtil: PhoneNumberUtil) {

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
                errorMessage = StringResource.FromString("Phone number cannot be empty.")
            )
        }
        return try {
            val phoneNumberProto: Phonenumber.PhoneNumber = phoneNumberUtil.parse(phone, regionCode)
            val isValid = phoneNumberUtil.isValidNumber(phoneNumberProto)
            if (isValid) {
                ValidationResult(isSuccessful = true)
            } else {
                ValidationResult(
                    isSuccessful = false,
                    errorMessage = StringResource.FromString("Invalid phone number for the selected region.")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ValidationResult(
                isSuccessful = false,
                errorMessage = StringResource.FromString("Invalid phone number format.")
            )
        }
    }
}