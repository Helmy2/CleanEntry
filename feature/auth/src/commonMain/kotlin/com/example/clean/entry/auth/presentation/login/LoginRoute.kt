package com.example.clean.entry.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cleanentry.feature.auth.generated.resources.Res
import cleanentry.feature.auth.generated.resources.continue_label
import cleanentry.feature.auth.generated.resources.create_account
import cleanentry.feature.auth.generated.resources.email_label
import cleanentry.feature.auth.generated.resources.email_placeholder
import cleanentry.feature.auth.generated.resources.login
import cleanentry.feature.auth.generated.resources.password_label
import cleanentry.feature.auth.generated.resources.password_placeholder
import cleanentry.feature.auth.generated.resources.phone_placeholder
import cleanentry.feature.auth.generated.resources.sign_up
import cleanentry.feature.auth.generated.resources.you_don_t_have_an_account
import com.example.clean.entry.auth.domain.model.AuthMethod
import com.example.clean.entry.auth.domain.model.Country
import com.example.clean.entry.auth.presentation.components.TopBar
import com.example.clean.entry.auth.presentation.components.TopBarWithBackNavigation
import com.example.clean.entry.core.components.AppButton
import com.example.clean.entry.core.components.AppTextField
import com.example.clean.entry.core.components.OtpTextField
import com.example.clean.entry.core.components.PasswordTextField
import com.example.clean.entry.core.components.PhoneTextField
import com.example.clean.entry.core.design_system.spacing
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = koinViewModel(), countryResult: Country?
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(countryResult) {
        if (countryResult != null) {
            viewModel.handleEvent(LoginReducer.Event.CountrySelected(countryResult))
        }
    }

    LoginScreen(
        state = state,
        onEvent = viewModel::handleEvent,
    )
}

@Composable
fun LoginScreen(
    state: LoginReducer.State,
    onEvent: (LoginReducer.Event) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Scaffold(
        topBar = {
            if (state.verificationId == null) TopBar(
                title = stringResource(Res.string.login)
            ) else TopBarWithBackNavigation(
                title = stringResource(Res.string.sign_up),
                onBackClick = { onEvent(LoginReducer.Event.BackButtonClicked) },
            )
        }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.medium,
                bottom = MaterialTheme.spacing.medium
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            if (state.verificationId == null) {
                TabRow(selectedTabIndex = state.authMethod.ordinal) {
                    AuthMethod.entries.forEach { method ->
                        Tab(
                            selected = state.authMethod == method,
                            onClick = { onEvent(LoginReducer.Event.AuthMethodChanged(method)) },
                            text = {
                                Text(
                                    method.name.replace("_", " ").lowercase()
                                        .replaceFirstChar { it.uppercase() })
                            },
                        )
                    }
                }

                when (state.authMethod) {
                    AuthMethod.EMAIL -> {
                        AppTextField(
                            value = state.email,
                            onValueChange = { onEvent(LoginReducer.Event.EmailChanged(it)) },
                            labelText = stringResource(Res.string.email_label),
                            isError = state.emailError != null,
                            supportingText = state.emailError,
                            placeholderText = stringResource(Res.string.email_placeholder),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                            )
                        )

                        PasswordTextField(
                            value = state.password,
                            onValueChange = { onEvent(LoginReducer.Event.PasswordChanged(it)) },
                            labelText = stringResource(Res.string.password_label),
                            isVisible = state.isPasswordVisible,
                            onVisibilityToggle = { onEvent(LoginReducer.Event.TogglePasswordVisibility) },
                            isError = state.passwordError != null,
                            supportingText = state.passwordError,
                            placeholderText = stringResource(Res.string.password_placeholder),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                            )
                        )
                    }

                    AuthMethod.PHONE -> {
                        PhoneTextField(
                            value = state.phone,
                            onValueChange = { onEvent(LoginReducer.Event.PhoneChanged(it)) },
                            onCountryCodeClick = { onEvent(LoginReducer.Event.CountryButtonClick) },
                            countryCode = state.selectedCountry.dialCode,
                            countryFlag = state.selectedCountry.flagEmoji,
                            isError = state.phoneError != null,
                            supportingText = state.phoneError,
                            placeholderText = stringResource(Res.string.phone_placeholder),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done
                            )
                        )
                    }
                }
            } else {
                OtpTextField(
                    otpText = state.otp,
                    onOtpTextChange = { otp, isComplete ->
                        onEvent(LoginReducer.Event.OtpChanged(otp))
                        if (isComplete) {
                            focusManager.clearFocus()
                            onEvent(LoginReducer.Event.Submit)
                        }
                    },
                    otpCount = state.otpCount,
                )
            }

            if (state.error != null) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = MaterialTheme.spacing.small)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            val buttonText = when {
                state.verificationId != null -> "Verify OTP"
                state.authMethod == AuthMethod.PHONE -> "Send OTP"
                else -> stringResource(Res.string.continue_label)
            }

            AppButton(
                text = buttonText,
                onClick = { onEvent(LoginReducer.Event.Submit) },
                enabled = state.isLoginButtonEnabled,
                isLoading = state.isLoading,
                modifier = Modifier.testTag("login_button")
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(MaterialTheme.spacing.medium),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.you_don_t_have_an_account),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stringResource(Res.string.create_account),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(onClick = { onEvent(LoginReducer.Event.CreateAccountClicked) })
                )
            }
        }
    }
}