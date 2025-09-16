package com.example.clean.entry.shared.util

import com.example.clean.entry.core.util.PhoneNumberVerifier

expect class PhoneNumberVerifierImpl : PhoneNumberVerifier {
    override fun isValidNumber(phone: String, regionCode: String): Boolean
}