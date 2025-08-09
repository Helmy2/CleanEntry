package com.example.clean.entry.feature_auth.util

actual class PhoneNumberUtil {
    actual fun isValidNumber(phone: String, regionCode: String): Boolean {
       return phoneNumberValidatorProvider.isValidNumber(phone, regionCode)
    }
}

lateinit var phoneNumberValidatorProvider: PhoneNumberValidatorProvider

interface PhoneNumberValidatorProvider {
    fun isValidNumber(phone: String, regionCode: String): Boolean
}