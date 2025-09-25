package com.example.clean.entry.core.util

expect object Secrets {
    fun getPexelsApiKey(): String
    fun getFirebaseApiKey(): String
}