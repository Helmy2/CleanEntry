package com.example.clean.entry.shared.util

import com.example.clean.entry.core.util.PhoneNumberVerifier
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber

actual class PhoneNumberVerifierImpl(
    private val phoneNumberUtil: PhoneNumberUtil
) : PhoneNumberVerifier {
    actual override fun isValidNumber(phone: String, regionCode: String): Boolean {
        val phoneNumberProto: Phonenumber.PhoneNumber = phoneNumberUtil.parse(phone, regionCode)
        return phoneNumberUtil.isValidNumber(phoneNumberProto)
    }
}