package com.example.clean.entry.core.util

import io.github.aakira.napier.Napier
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE

object KoinNapierLogger : Logger() {
    override fun display(level: Level, msg: MESSAGE) {
        when (level) {
            Level.DEBUG -> Napier.d("Koin: $msg")
            Level.INFO -> Napier.i("Koin: $msg")
            Level.ERROR -> Napier.e("Koin: $msg")
            Level.WARNING -> Napier.w("Koin: $msg")
            Level.NONE -> {}
        }
    }
}