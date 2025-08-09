package com.example.clean.entry.feature_auth.presentation.login

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cleanentry.composeapp.generated.resources.Res
import cleanentry.composeapp.generated.resources.continue_label
import cleanentry.composeapp.generated.resources.create_account
import cleanentry.composeapp.generated.resources.login
import cleanentry.composeapp.generated.resources.password_label
import cleanentry.composeapp.generated.resources.password_placeholder
import cleanentry.composeapp.generated.resources.phone_placeholder
import cleanentry.composeapp.generated.resources.please_fill_the_details_and_log_in
import cleanentry.composeapp.generated.resources.you_don_t_have_an_account
import com.example.clean.entry.core.components.PasswordTextField
import com.example.clean.entry.core.components.PhoneTextField
import com.example.clean.entry.core.design_system.spacing
import com.example.clean.entry.core.domain.model.stringResource
import com.example.clean.entry.core.ui.ObserveEffect
import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.presentation.components.NativeAppButton
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * The stateful "Route" for the Login screen.
 * This composable is responsible for connecting to the ViewModel and handling side effects.
 */
@Composable
fun LoginRoute(
    viewModel: LoginViewModel = koinViewModel(),
    onNavigateToCountryPicker: (Country) -> Unit,
    onLoginSuccess: () -> Unit,
    onCreateAccountClick: () -> Unit,
    countryResult: Country?
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEffect(viewModel.effect) { effect ->
        when (effect) {
            is LoginReducer.Effect.LoginSuccess -> {
                onLoginSuccess()
            }
        }
    }

    LaunchedEffect(countryResult) {
        if (countryResult != null) {
            viewModel.handleEvent(LoginReducer.Event.CountrySelected(countryResult))
        }
    }

    LoginScreen(
        state = state,
        onEvent = viewModel::handleEvent,
        onCountryCodeClick = {
            onNavigateToCountryPicker(
                Country(
                    dialCode = state.selectedCountryDialCode,
                    code = state.selectedCountryCode,
                    flagEmoji = state.selectedCountryFlag,
                    name = ""
                )
            )
        },
        onCreateAccountClick = onCreateAccountClick
    )
}

@Composable
fun LoginScreen(
    state: LoginReducer.State,
    onEvent: (LoginReducer.Event) -> Unit,
    onCountryCodeClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {

    Scaffold(
        topBar = {
            Text(
                text = stringResource(Res.string.login),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.medium
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.please_fill_the_details_and_log_in),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))


            PhoneTextField(
                value = state.phone,
                onValueChange = { onEvent(LoginReducer.Event.PhoneChanged(it)) },
                onCountryCodeClick = onCountryCodeClick,
                countryCode = state.selectedCountryDialCode,
                countryFlag = state.selectedCountryFlag,
                isError = state.phoneError == null,
                supportingText = state.phoneError?.let { stringResource(it) },
                placeholderText = stringResource(Res.string.phone_placeholder),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))

            PasswordTextField(
                value = state.password,
                onValueChange = { onEvent(LoginReducer.Event.PasswordChanged(it)) },
                labelText = stringResource(Res.string.password_label),
                isVisible = state.isPasswordVisible,
                onVisibilityToggle = { onEvent(LoginReducer.Event.TogglePasswordVisibility) },
                isError = state.passwordError != null,
                supportingText = state.passwordError?.let { stringResource(it) },
                placeholderText = stringResource(Res.string.password_placeholder),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            NativeAppButton(
                text = stringResource(Res.string.continue_label),
                onClick = { onEvent(LoginReducer.Event.LoginClicked) },
                enabled = state.isLoginButtonEnabled,
                isLoading = state.isLoading,
                modifier = Modifier.testTag("login_button")
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium),
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
                    modifier = Modifier.clickable(onClick = onCreateAccountClick)
                )
            }
        }
    }
}
