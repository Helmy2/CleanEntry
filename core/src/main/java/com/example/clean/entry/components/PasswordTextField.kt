package com.example.clean.entry.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.clean.core.R

/**
 * A specialized AppTextField for password input.
 *
 * This component builds on top of AppTextField to provide password-specific
 * functionality, such as a visibility toggle icon.
 *
 * @param value The input password to be shown in the text field.
 * @param onValueChange The callback that is triggered when the input service updates the text.
 * @param modifier The modifier to be applied to the text field.
 * @param labelText The text to be used for the label.
 * @param isVisible Whether the password text is currently visible.
 * @param onVisibilityToggle The callback that is triggered when the visibility icon is clicked.
 * @param isError Indicates if the text field's current value is in error.
 * @param supportingText The supporting text to be displayed below the text field.
 */
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String,
    isVisible: Boolean,
    onVisibilityToggle: () -> Unit,
    isError: Boolean = false,
    supportingText: String? = null,
    placeholderText: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        labelText = labelText,
        isError = isError,
        supportingText = supportingText,
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        placeholderText = placeholderText,
        keyboardOptions = keyboardOptions,
        trailingIcon = {
            val image = if (isVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description = if (isVisible) stringResource(R.string.hide_password) else stringResource(
                R.string.show_password
            )

            IconButton(onClick = onVisibilityToggle) {
                Icon(imageVector = image, contentDescription = description)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PasswordTextFieldPreview() {
    PasswordTextField(
        value = "password",
        onValueChange = {},
        labelText = "Password",
        isVisible = true,
        onVisibilityToggle = {},
        placeholderText = "Enter your password",
        supportingText = "Enter your password",
    )
}

@Preview(showBackground = true)
@Composable
fun PasswordTextFieldErrorPreview() {
    PasswordTextField(
        value = "password",
        onValueChange = {},
        labelText = "Password",
        isVisible = true,
        onVisibilityToggle = {},
        isError = true,
        placeholderText = "Enter your password",
        supportingText = "Error message",
    )
}
