package com.example.clean.entry.feature_auth.util

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber


actual class PhoneNumberUtil(
    private val phoneNumberUtil: PhoneNumberUtil
) {
    actual fun isValidNumber(phone: String, regionCode: String): Boolean {
        val phoneNumberProto: Phonenumber.PhoneNumber = phoneNumberUtil.parse(phone, regionCode)
        return phoneNumberUtil.isValidNumber(phoneNumberProto)
    }
}