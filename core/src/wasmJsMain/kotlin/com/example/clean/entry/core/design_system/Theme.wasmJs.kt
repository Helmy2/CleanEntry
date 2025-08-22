package com.example.clean.entry.core.design_system

@androidx.compose.runtime.Composable
actual fun getColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean
): androidx.compose.material3.ColorScheme {
    return if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
}