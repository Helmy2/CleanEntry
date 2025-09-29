package com.example.clean.entry.details.platform

// Import org.w3c types directly
import androidx.compose.runtime.Composable
import kotlinx.browser.window

@Composable
actual fun StartImageDownload(
    imageUrl: String,
    title: String,
    description: String
) {
    window.open(imageUrl, "_blank")
}