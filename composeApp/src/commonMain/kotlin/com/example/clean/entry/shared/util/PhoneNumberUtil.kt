package com.example.clean.entry.shared.util

expect class PhoneNumberUtil{
    fun isValidNumber(phone: String, regionCode: String): Boolean
}