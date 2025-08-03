package com.example.clean.entry.core.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import cleanentry.core.generated.resources.Res
import cleanentry.core.generated.resources.select_country_code
import com.example.clean.entry.core.design_system.spacing
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A specialized text field for phone number input.
 *
 * This component provides a standard text field for the phone number and a
 * leading composable for selecting the country code, complete with a flag.
 * It reuses the AppTextField for a consistent look and feel.
 *
 * @param value The phone number text to be shown in the text field.
 * @param onValueChange The callback that is triggered when the input service updates the text.
 * @param onCountryCodeClick The callback that is triggered when the country code selector is clicked.
 * @param countryCode The currently selected country's dial code (e.g., "+20").
 * @param countryFlag The emoji string for the currently selected country's flag.
 * @param modifier The modifier to be applied to the text field.
 * @param isError Indicates if the text field's current value is in error.
 * @param supportingText The supporting text to be displayed below the text field.
 */
@Composable
fun PhoneTextField(
    placeholderText: String,
    value: String,
    onValueChange: (String) -> Unit,
    onCountryCodeClick: () -> Unit,
    countryCode: String,
    countryFlag: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
    supportingText: String? = null,
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        isError = isError,
        supportingText = supportingText,
        keyboardOptions = keyboardOptions,
        placeholderText = placeholderText,
        leadingIcon = {
            Row(
                modifier = Modifier.clickable(onClick = onCountryCodeClick),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))


                Text(
                    text = countryFlag,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))


                Text(
                    text = countryCode,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(Res.string.select_country_code)
                )
            }
        }
    )
}

@Preview
@Composable
fun PhoneTextFieldPreview() {
    PhoneTextField(
        value = "123456789",
        onValueChange = {},
        onCountryCodeClick = {},
        countryCode = "+1",
        countryFlag = "🇺🇸",
        placeholderText = "Enter your phone number",
    )
}




