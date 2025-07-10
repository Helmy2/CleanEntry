package com.example.clean.feature_auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.clean.core.components.AppButton
import com.example.clean.core.components.PasswordTextField
import com.example.clean.core.components.PhoneTextField
import com.example.clean.core.design_system.spacing
import com.example.clean.core.ui.ObserveEffect
import com.example.clean.core.util.stringResource
import com.example.clean.feature_auth.R
import org.koin.compose.viewmodel.koinViewModel

/**
 * The stateful "Route" for the Login screen.
 * This composable is responsible for connecting to the ViewModel and handling side effects.
 */
@Composable
fun LoginRoute(
    viewModel: LoginViewModel = koinViewModel(),
    onNavigateToCountryPicker: () -> Unit,
    onLoginSuccess: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEffect(viewModel.effect) { effect ->
        when (effect) {
            is LoginReducer.Effect.LoginSuccess -> {
                onLoginSuccess()
            }
        }
    }

    LoginScreen(
        state = state,
        onEvent = viewModel::processEvent,
        onCountryCodeClick = onNavigateToCountryPicker,
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
                text = stringResource(R.string.login),
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
                text = stringResource(R.string.please_fill_the_details_and_log_in),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))


            PhoneTextField(
                value = state.phone,
                onValueChange = { onEvent(LoginReducer.Event.PhoneChanged(it)) },
                onCountryCodeClick = onCountryCodeClick,
                countryCode = state.selectedCountryDialCode,
                countryFlag = "🇪🇬",
                isError = state.phoneError == null,
                supportingText = state.phoneError?.let { stringResource(it) },
                placeholderText = stringResource(R.string.phone_placeholder),
            )

            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))

            PasswordTextField(
                value = state.password,
                onValueChange = { onEvent(LoginReducer.Event.PasswordChanged(it)) },
                labelText = stringResource(R.string.password_label),
                isVisible = state.isPasswordVisible,
                onVisibilityToggle = { onEvent(LoginReducer.Event.TogglePasswordVisibility) },
                isError = state.passwordError != null,
                supportingText = state.passwordError?.let { stringResource(it) },
                placeholderText = stringResource(R.string.password_placeholder)
            )

            Spacer(modifier = Modifier.weight(1f))

            AppButton(
                text = stringResource(R.string.continue_label),
                onClick = { onEvent(LoginReducer.Event.LoginClicked) },
                enabled = state.isLoginButtonEnabled,
                isLoading = state.isLoading
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.you_don_t_have_an_account),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stringResource(R.string.create_account),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(onClick = onCreateAccountClick)
                )
            }
        }
    }
}
