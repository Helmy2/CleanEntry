package com.example.clean.entry.feature_auth.presentation.registration

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.clean.core.components.AppButton
import com.example.clean.core.components.AppTextField
import com.example.clean.core.components.PhoneTextField
import com.example.clean.core.design_system.spacing
import com.example.clean.core.ui.ObserveEffect
import com.example.clean.core.util.stringResource
import com.example.clean.entry.feature_auth.presentation.components.TopBarWithBackNavigation
import com.example.clean.feature_auth.R
import org.koin.compose.viewmodel.koinViewModel

/**
 * The stateful "Route" for the Registration screen.
 * This composable is responsible for connecting to the ViewModel and handling side effects.
 */
@Composable
fun RegistrationRoute(
    viewModel: RegistrationViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onNavigateToCountryPicker: () -> Unit,
    onRegistrationSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEffect(viewModel.effect) { effect ->
        when (effect) {
            is RegistrationReducer.Effect.RegistrationSuccess -> {
                onRegistrationSuccess()
            }
        }
    }

    RegistrationScreen(
        state = state,
        onEvent = viewModel::processEvent,
        onCountryCodeClick = onNavigateToCountryPicker,
        onBackClick = onBackClick
    )
}


@Composable
fun RegistrationScreen(
    state: RegistrationReducer.State,
    onEvent: (RegistrationReducer.Event) -> Unit,
    onCountryCodeClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBarWithBackNavigation(
                title = stringResource(R.string.sign_up),
                onBackClick = onBackClick,
                modifier = Modifier.padding(MaterialTheme.spacing.medium)
            )
        }
    ) { padding ->
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

            AppTextField(
                value = state.firstName,
                onValueChange = { onEvent(RegistrationReducer.Event.FirstNameChanged(it)) },
                labelText = stringResource(R.string.name_label),
                placeholderText = stringResource(R.string.name_placeholder),
                isError = state.firstNameError != null,
                supportingText = state.firstNameError?.let { stringResource(it) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            AppTextField(
                value = state.surname,
                onValueChange = { onEvent(RegistrationReducer.Event.SurnameChanged(it)) },
                labelText = stringResource(R.string.surname_label),
                placeholderText = stringResource(R.string.surname_placeholder),
                isError = state.surnameError != null,
                supportingText = state.surnameError?.let { stringResource(it) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            AppTextField(
                value = state.email,
                onValueChange = { onEvent(RegistrationReducer.Event.EmailChanged(it)) },
                labelText = stringResource(R.string.email_label),
                isError = state.emailError != null,
                supportingText = state.emailError?.let { stringResource(it) },
                placeholderText = stringResource(R.string.email_placeholder),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            PhoneTextField(
                value = state.phone,
                onValueChange = { onEvent(RegistrationReducer.Event.PhoneChanged(it)) },
                onCountryCodeClick = onCountryCodeClick,
                countryCode = "+20",
                countryFlag = "🇪🇬",
                isError = state.phoneError != null,
                supportingText = state.phoneError?.let { stringResource(it) },
                placeholderText = androidx.compose.ui.res.stringResource(R.string.phone_placeholder),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            AppButton(
                text = stringResource(R.string.continue_label),
                onClick = { onEvent(RegistrationReducer.Event.Submit) },
                enabled = state.isContinueButtonEnabled,
                isLoading = state.isLoading,
            )
        }
    }
}