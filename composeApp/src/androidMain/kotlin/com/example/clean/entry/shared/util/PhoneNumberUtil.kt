package com.example.clean.entry.shared.util

import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber

actual class PhoneNumberUtil(
    private val phoneNumberUtil: PhoneNumberUtil
) {
    actual fun isValidNumber(phone: String, regionCode: String): Boolean {
        val phoneNumberProto: Phonenumber.PhoneNumber = phoneNumberUtil.parse(phone, regionCode)
        return phoneNumberUtil.isValidNumber(phoneNumberProto)
    }
}