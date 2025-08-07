package com.example.clean.entry.feature_auth.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitViewController
import com.example.clean.entry.feature_auth.presentation.login.LocalNativeViewFactory
import com.example.clean.entry.feature_auth.util.NativeButtonController

@Composable
actual fun NativeAppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    isLoading: Boolean
) {
    val factory = LocalNativeViewFactory.current

    UIKitViewController(
        factory = {
            factory.createAppButton(text = text, onClick = onClick)
        },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        update = { viewController ->
            val appButtonController = viewController as? NativeButtonController

            appButtonController?.setEnabled(enabled = enabled)
            appButtonController?.setIsLoading(isLoading = isLoading)
        }
    )
}

