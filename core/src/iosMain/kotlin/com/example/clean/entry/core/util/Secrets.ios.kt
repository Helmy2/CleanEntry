package com.example.clean.entry.core.util

import Secrets.getFirebaseApiKeyFromNative
import Secrets.getPexelsApiKeyFromNative
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString


@OptIn(ExperimentalForeignApi::class)
actual object Secrets {
    actual fun getPexelsApiKey(): String =
        getPexelsApiKeyFromNative()?.toKString() ?: ""

    actual fun getFirebaseApiKey(): String =
        getFirebaseApiKeyFromNative()?.toKString() ?: ""
}