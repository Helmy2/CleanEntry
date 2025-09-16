package com.example.clean.entry.shared.util

import com.example.clean.entry.core.util.PhoneNumberVerifier

actual class PhoneNumberVerifierImpl : PhoneNumberVerifier {
    actual override fun isValidNumber(phone: String, regionCode: String): Boolean {
       return phoneNumberValidatorProvider.isValidNumber(phone, regionCode)
    }
}

lateinit var phoneNumberValidatorProvider: PhoneNumberValidatorProvider

interface PhoneNumberValidatorProvider {
    fun isValidNumber(phone: String, regionCode: String): Boolean
}