package com.example.clean.entry.shared.util

import com.example.clean.entry.core.util.PhoneNumberVerifier

external interface JsPhoneNumber

external interface JsPhoneNumberUtil {
    fun parseAndKeepRawInput(numberToParse: String, defaultRegion: String?): JsPhoneNumber
    fun isValidNumber(number: JsPhoneNumber): Boolean
}

external interface JsPhoneNumberUtilCompanion {
    fun getInstance(): JsPhoneNumberUtil
}

@JsModule("google-libphonenumber")
external object GooglePhoneNumberModule {
    val PhoneNumberUtil: JsPhoneNumberUtilCompanion
}

actual class PhoneNumberVerifierImpl : PhoneNumberVerifier {
    private val phoneUtil = GooglePhoneNumberModule.PhoneNumberUtil.getInstance()
    actual override fun isValidNumber(phone: String, regionCode: String): Boolean {
        return try {
            val phoneNumber = phoneUtil.parseAndKeepRawInput(phone, regionCode)
            phoneUtil.isValidNumber(phoneNumber)
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }
    }
}