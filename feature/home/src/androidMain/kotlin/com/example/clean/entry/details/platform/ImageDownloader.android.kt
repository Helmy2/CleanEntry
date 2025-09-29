package com.example.clean.entry.details.platform

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import coil3.compose.LocalPlatformContext

private const val REQUEST_WRITE_STORAGE_PERMISSION = 1001

/**
 * Android-specific implementation to initiate an image download.
 */
@Composable
actual fun StartImageDownload(
    imageUrl: String,
    title: String,
    description: String
) {
    // This is a simplified context retrieval. In a real app,
    // you might need to pass context or get it from a Koin-injected Application context.
    // For now, assuming this function is called from a context that can provide an Activity.
    // This is a placeholder and needs a proper way to get context in your architecture.

    val context = LocalPlatformContext.current

    LaunchedEffect(
        imageUrl
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (context is Activity) {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_STORAGE_PERMISSION
                )
                Toast.makeText(
                    context,
                    "Storage permission needed. Please grant and try again.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Storage permission needed. Cannot request from this context.",
                    Toast.LENGTH_LONG
                ).show()
            }
            return@LaunchedEffect
        }

        try {
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request(imageUrl.toUri())
                .setTitle(title)
                .setDescription(description)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "$title.jpg"
                ) // Ensure title is a safe filename
                .setAllowedOverMetered(true) // Allow download over metered connection
                .setAllowedOverRoaming(true)

            downloadManager.enqueue(request)
            Toast.makeText(context, "Download started: $title", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Download failed: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}