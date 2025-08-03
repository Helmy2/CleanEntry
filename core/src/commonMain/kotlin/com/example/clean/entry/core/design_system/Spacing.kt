package com.example.clean.entry.core.design_system

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A data class that holds the spacing values for the app.
 * This provides a consistent scale for padding, margins, and arrangement gaps.
 */
data class Spacing(
    val default: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 32.dp,
    val extraLarge: Dp = 64.dp
)

/**
 * A CompositionLocal that provides the Spacing object down the composable tree.
 * This allows any composable within our theme to access the spacing values.
 */
val LocalSpacing = staticCompositionLocalOf { Spacing() }

/**
 * An extension property on MaterialTheme to easily access the custom spacing values.
 * e.g., MaterialTheme.spacing.medium
 */
val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current
