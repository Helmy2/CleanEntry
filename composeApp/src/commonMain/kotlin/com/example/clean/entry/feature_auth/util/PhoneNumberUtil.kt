package com.example.clean.entry.feature_auth.util

expect class PhoneNumberUtil{
    fun isValidNumber(phone: String, regionCode: String): Boolean
}