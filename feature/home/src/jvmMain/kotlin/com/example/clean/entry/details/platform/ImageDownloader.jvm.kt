package com.example.clean.entry.details.platform


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.clean.entry.core.util.Secrets
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView

@Composable
actual fun StartImageDownload(
    imageUrl: String,
    title: String,
    description: String
) {
    LaunchedEffect(imageUrl) {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection // Cast to HttpURLConnection

            // *** ADD PEXELS API KEY HERE ***
            val pexelsApiKey = Secrets.getPexelsApiKey() // Get your API key
            if (pexelsApiKey.isNotBlank()) {
                connection.setRequestProperty("Authorization", pexelsApiKey)
            } else {
                println("PEXELS API KEY IS MISSING. Download might fail.")
                // Optionally, you could prevent the download or show a UI error
            }
            // It's also good practice to set a User-Agent
            connection.setRequestProperty("User-Agent", "CleanEntryApp/1.0")


            connection.connectTimeout = 15000 // 15 seconds
            connection.readTimeout = 15000    // 15 seconds
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                println("Server returned HTTP $responseCode: ${connection.responseMessage}")
                // Read error stream for more details if available
                var errorDetails = "No details from error stream."
                connection.errorStream?.bufferedReader()?.use {
                    errorDetails = it.readText()
                }
                println("Error details: $errorDetails")

                // Potentially handle specific codes, e.g., 401/403 for auth issues, 404 for not found
                if (responseCode == HttpURLConnection.HTTP_FORBIDDEN || responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    println("Authentication/Authorization failed. Check your Pexels API Key setup.")
                }
                return@LaunchedEffect // Stop here if not HTTP_OK
            }


            val inputStream: InputStream = connection.inputStream

            val defaultFileName = title.replace(Regex("[^a-zA-Z0-9._-]"), "_") + ".jpg"
            val fileChooser = JFileChooser(FileSystemView.getFileSystemView().homeDirectory)
            fileChooser.dialogTitle = "Save Image As..."
            fileChooser.selectedFile = File(defaultFileName)
            fileChooser.isAcceptAllFileFilterUsed = false

            val userSelection = fileChooser.showSaveDialog(null)

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                var saveFile = fileChooser.selectedFile
                if (!saveFile.name.contains('.')) {
                    saveFile = File(saveFile.parentFile, saveFile.name + ".jpg")
                } else if (!saveFile.name.endsWith(".jpg", ignoreCase = true)) {
                    // Potentially warn or adjust
                }

                FileOutputStream(saveFile).use { outputStream ->
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }
                println("Image downloaded successfully to: ${saveFile.absolutePath}")
            } else {
                println("Image download cancelled by user.")
            }
            inputStream.close()

        } catch (e: Exception) {
            println("Error downloading image for desktop: ${e.message}")
            e.printStackTrace()
        }
    }
}