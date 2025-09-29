package com.example.clean.entry.details.platform

import androidx.compose.runtime.Composable


/**
 * Platform-specific function to initiate an image download.
 * @param imageUrl The URL of the image to download.
 * @param title The title for the download (e.g., used for notification and filename).
 * @param description A description for the download.
 */
@Composable
expect fun StartImageDownload(
    imageUrl: String,
    title: String,
    description: String
)

