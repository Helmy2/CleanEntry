package com.example.clean.core.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

/**
 * A custom Modifier that applies a shimmering animation to any composable.
 * This is used to create a placeholder loading effect.
 *
 * @param isLoading Whether the shimmer effect should be shown.
 * @param shimmerColors The list of colors to use for the shimmer gradient.
 * @param animationDuration The duration of one shimmer animation cycle in milliseconds.
 * @return A Modifier that applies the shimmer effect.
 */
fun Modifier.shimmer(
    isLoading: Boolean = true,
    shimmerColors: List<Color> = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    ),
    animationDuration: Int = 1000
): Modifier = composed {
    if (!isLoading) {
        return@composed this
    }

    var size by remember { mutableStateOf(IntSize.Zero) }

    val transition = rememberInfiniteTransition(label = "ShimmerTransition")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDuration)
        ),
        label = "ShimmerStartOffsetX"
    )

    background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned { layoutCoordinates ->
        size = layoutCoordinates.size
    }
}
