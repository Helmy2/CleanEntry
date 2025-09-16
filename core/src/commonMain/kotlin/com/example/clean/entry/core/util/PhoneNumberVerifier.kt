package com.example.clean.entry.core.util

interface PhoneNumberVerifier {
    fun isValidNumber(phone: String, regionCode: String): Boolean
}