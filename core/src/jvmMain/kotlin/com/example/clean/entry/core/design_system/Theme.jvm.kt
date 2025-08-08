package com.example.clean.entry.core.design_system

import androidx.compose.material3.ColorScheme

@androidx.compose.runtime.Composable
actual fun getColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean
): ColorScheme {
    return if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
}