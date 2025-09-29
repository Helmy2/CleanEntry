package com.example.clean.entry

import com.example.clean.entry.core.util.PhoneNumberVerifier
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber


class PhoneNumberVerifierImpl(
    private val phoneNumberUtil: PhoneNumberUtil
) : PhoneNumberVerifier {
    override fun isValidNumber(phone: String, regionCode: String): Boolean {
        val phoneNumberProto: Phonenumber.PhoneNumber = phoneNumberUtil.parse(phone, regionCode)
        return phoneNumberUtil.isValidNumber(phoneNumberProto)
    }
}