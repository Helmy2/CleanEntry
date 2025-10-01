@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.example.clean.entry.core.util

expect object Secrets {
    fun getPexelsApiKey(): String
    fun getFirebaseApiKey(): String
}