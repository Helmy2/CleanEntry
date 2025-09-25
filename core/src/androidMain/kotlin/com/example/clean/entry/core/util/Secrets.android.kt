package com.example.clean.entry.core.util

actual object Secrets {

    init {
        System.loadLibrary("native-lib")
    }

    private external fun getPexelsApiKeyFromNative(): String
    private external fun getFirebaseApiKeyFromNative(): String

    actual fun getPexelsApiKey(): String {
        return getPexelsApiKeyFromNative()
    }

    actual fun getFirebaseApiKey(): String {
        return getFirebaseApiKeyFromNative()
    }
}