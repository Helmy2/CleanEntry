package com.example.clean.entry.auth.presentation.registration

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cleanentry.feature.auth.generated.resources.Res
import cleanentry.feature.auth.generated.resources.confirm_password_label
import cleanentry.feature.auth.generated.resources.confirm_password_placeholder
import cleanentry.feature.auth.generated.resources.continue_label
import cleanentry.feature.auth.generated.resources.email_label
import cleanentry.feature.auth.generated.resources.email_placeholder
import cleanentry.feature.auth.generated.resources.password_label
import cleanentry.feature.auth.generated.resources.password_placeholder
import cleanentry.feature.auth.generated.resources.phone_placeholder
import cleanentry.feature.auth.generated.resources.sign_up
import com.example.clean.entry.auth.domain.model.AuthMethod
import com.example.clean.entry.auth.domain.model.Country
import com.example.clean.entry.core.components.AppButton
import com.example.clean.entry.core.components.AppTextField
import com.example.clean.entry.core.components.OtpTextField
import com.example.clean.entry.core.components.PhoneTextField
import com.example.clean.entry.core.components.TopBarWithBackNavigation
import com.example.clean.entry.core.design_system.spacing
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegistrationRoute(
    viewModel: RegistrationViewModel = koinViewModel(),
    countryResult: Country?,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(countryResult) {
        if (countryResult != null) {
            viewModel.handleEvent(RegistrationReducer.Event.CountrySelected(countryResult))
        }
    }

    RegistrationScreen(
        state = state, onEvent = viewModel::handleEvent,
    )
}


@Composable
fun RegistrationScreen(
    state: RegistrationReducer.State,
    onEvent: (RegistrationReducer.Event) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Scaffold(
        topBar = {
            TopBarWithBackNavigation(
                title = stringResource(Res.string.sign_up),
                onBackClick = {
                    onEvent(RegistrationReducer.Event.BackButtonClicked)
                },
            )
        }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.medium
                )
                .padding(padding)
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {

            if (state.verificationId == null) {
                TabRow(selectedTabIndex = state.authMethod.ordinal) {
                    AuthMethod.entries.forEach { method ->
                        Tab(
                            selected = state.authMethod == method,
                            onClick = { onEvent(RegistrationReducer.Event.AuthMethodChanged(method)) },
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
                            onValueChange = { onEvent(RegistrationReducer.Event.EmailChanged(it)) },
                            labelText = stringResource(Res.string.email_label),
                            isError = state.emailError != null,
                            supportingText = state.emailError,
                            placeholderText = stringResource(Res.string.email_placeholder),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                            )
                        )
                        AppTextField(
                            value = state.password,
                            onValueChange = { onEvent(RegistrationReducer.Event.PasswordChanged(it)) },
                            labelText = stringResource(Res.string.password_label),
                            placeholderText = stringResource(Res.string.password_placeholder),
                            isError = state.passwordError != null,
                            supportingText = state.passwordError,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                            )
                        )
                        AppTextField(
                            value = state.confirmPassword,
                            onValueChange = {
                                onEvent(
                                    RegistrationReducer.Event.ConfirmPasswordChanged(
                                        it
                                    )
                                )
                            },
                            labelText = stringResource(Res.string.confirm_password_label),
                            placeholderText = stringResource(Res.string.confirm_password_placeholder),
                            isError = state.confirmPasswordError != null,
                            supportingText = state.confirmPasswordError,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = { onEvent(RegistrationReducer.Event.Submit) })
                        )
                    }

                    AuthMethod.PHONE -> {
                        PhoneTextField(
                            value = state.phone,
                            onValueChange = { onEvent(RegistrationReducer.Event.PhoneChanged(it)) },
                            onCountryCodeClick = { onEvent(RegistrationReducer.Event.CountryButtonClick) },
                            countryCode = state.selectedCountry.dialCode,
                            countryFlag = state.selectedCountry.flagEmoji,
                            isError = state.phoneError != null,
                            supportingText = state.phoneError,
                            placeholderText = stringResource(Res.string.phone_placeholder),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = { onEvent(RegistrationReducer.Event.Submit) })
                        )
                    }
                }
            } else {
                OtpTextField(
                    otpText = state.otp,
                    onOtpTextChange = { otp, isComplete ->
                        onEvent(RegistrationReducer.Event.OtpChanged(otp))
                        if (isComplete) {
                            focusManager.clearFocus()
                            onEvent(RegistrationReducer.Event.Submit)
                        }
                    },
                    otpCount = state.otpCount
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
                onClick = { onEvent(RegistrationReducer.Event.Submit) },
                enabled = state.isContinueButtonEnabled,
                isLoading = state.isLoading,
            )
        }
    }
}