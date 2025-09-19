package com.example.clean.entry.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.clean.entry.core.design_system.spacing
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * A general-purpose composable for displaying a full-screen error message with a retry button.
 *
 * @param message The error message to display.
 * @param onRetry A lambda to be invoked when the user clicks the "Retry" button.
 * @param modifier The modifier to be applied to the container.
 */
@Composable
fun ErrorScreen(
    message: StringResource,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(message),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
        AppButton(
            text = "Retry",
            onClick = onRetry,
            modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
        )
    }
}

/**
 * A general-purpose composable for displaying a full-screen error message with a retry button.
 *
 * @param message The error message to display.
 * @param onRetry A lambda to be invoked when the user clicks the "Retry" button.
 * @param modifier The modifier to be applied to the container.
 */
@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
        AppButton(
            text = "Retry",
            onClick = onRetry,
            modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
        )
    }
}
