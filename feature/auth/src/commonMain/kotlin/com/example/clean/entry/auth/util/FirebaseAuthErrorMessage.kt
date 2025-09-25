package com.example.clean.entry.auth.util

fun getReadableFirebaseAuthErrorMessage(rawMessage: String): String {
    return when {
        rawMessage.contains("INVALID_CODE") -> "Invalid OTP. Please try again."
        rawMessage.contains("EMAIL_EXISTS") -> "This email address is already in use."
        rawMessage.contains("INVALID_PASSWORD") -> "Incorrect password. Please try again."
        rawMessage.contains("EMAIL_NOT_FOUND") -> "No account found with this email address."
        rawMessage.contains("TOO_MANY_ATTEMPTS_TRY_LATER") -> "Too many attempts. Please try again later."
        else -> "An unknown error occurred. Please try again."
    }
}
