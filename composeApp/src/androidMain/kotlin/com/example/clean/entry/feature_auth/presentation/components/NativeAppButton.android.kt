package com.example.clean.entry.feature_auth.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.clean.entry.core.components.AppButton

@Composable
actual fun NativeAppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    isLoading: Boolean
) {
    AppButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        isLoading = isLoading
    )
}
