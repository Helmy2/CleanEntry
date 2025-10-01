package com.example.clean.entry.core.util

import io.github.aakira.napier.Napier
import io.ktor.client.plugins.logging.Logger

class KtorNapierLogger : Logger {
    override fun log(message: String) {
        Napier.i("Ktor Client: $message")
    }
}