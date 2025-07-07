package com.example.clean.core.design_system


import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color


val PrimaryGreen = Color(0xFF4CAF50)
val DarkButtonGray = Color(0xFF616161)
val LightGrayBorder = Color(0xFFBDBDBD)
val TextBlack = Color(0xFF1C1B1F)
val BackgroundLight = Color(0xFFFFFFFF)
val BackgroundDark = Color(0xFF121212)



/**
 * The Light Color Scheme for the app.
 * This defines the set of colors to be used in Light Mode.
 */
val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    secondary = DarkButtonGray,
    background = BackgroundLight,
    surface = BackgroundLight,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextBlack,
    onSurface = TextBlack,
    outline = LightGrayBorder
)

/**
 * The Dark Color Scheme for the app.
 * This defines the set of colors to be used in Dark Mode.
 */
val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen,
    secondary = LightGrayBorder,
    background = BackgroundDark,
    surface = BackgroundDark,
    onPrimary = Color.White,
    onSecondary = TextBlack,
    onBackground = Color.White,
    onSurface = Color.White,
    outline = DarkButtonGray
)