package com.example.clean.entry.core.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A custom, reusable text field component for the ArchGuard application.
 *
 * This OutlinedTextField is styled according to the app's design system and provides
 * a consistent look for all text input fields. It also supports displaying error states.
 *
 * @param value The input text to be shown in the text field.
 * @param onValueChange The callback that is triggered when the input service updates the text.
 * @param modifier The modifier to be applied to the text field.
 * @param labelText The text to be used for the label.
 * @param keyboardOptions Software keyboard options that contains configuration such as [androidx.compose.ui.text.input.KeyboardType] and [androidx.compose.ui.text.input.ImeAction].
 * @param isError Indicates if the text field's current value is in error.
 * @param visualTransformation Transforms the visual representation of the input text. For example, can be used to hide password characters.
 * @param trailingIcon The optional trailing icon to be displayed at the end of the text field.
 * @param leadingIcon The optional leading icon to be displayed at the beginning of the text field.
 * @param supportingText The supporting text to be displayed below the text field (e.g., an error message).
 */
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String? = null,
    placeholderText: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    supportingText: String? = null
) {
    Column {
        if (labelText != null)
            Text(labelText)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon,
            isError = isError,
            placeholder = {
                Text(
                    text = placeholderText,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            supportingText =
                if (supportingText != null) {
                    {
                        Text(
                            text = supportingText,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.6f
                            )
                        )
                    }
                } else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error,
            ),
            singleLine = true
        )
    }
}

@Preview
@Composable
fun AppTextFieldPreview() {
    AppTextField(
        value = "Input Text",
        onValueChange = {},
        labelText = "Label",
        placeholderText = "Placeholder",
        supportingText = "This is a supporting text."
    )
}


