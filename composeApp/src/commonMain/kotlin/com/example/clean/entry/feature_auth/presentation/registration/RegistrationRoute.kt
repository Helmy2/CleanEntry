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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cleanentry.composeapp.generated.resources.Res
import cleanentry.composeapp.generated.resources.continue_label
import cleanentry.composeapp.generated.resources.email_label
import cleanentry.composeapp.generated.resources.email_placeholder
import cleanentry.composeapp.generated.resources.name_label
import cleanentry.composeapp.generated.resources.name_placeholder
import cleanentry.composeapp.generated.resources.phone_placeholder
import cleanentry.composeapp.generated.resources.sign_up
import cleanentry.composeapp.generated.resources.surname_label
import cleanentry.composeapp.generated.resources.surname_placeholder
import com.example.clean.entry.core.components.AppButton
import com.example.clean.entry.core.components.AppTextField
import com.example.clean.entry.core.components.PhoneTextField
import com.example.clean.entry.core.design_system.spacing
import com.example.clean.entry.core.ui.ObserveEffect
import com.example.clean.entry.core.domain.model.stringResource
import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.presentation.components.TopBarWithBackNavigation
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * The stateful "Route" for the Registration screen.
 * This composable is responsible for connecting to the ViewModel and handling side effects.
 */
@Composable
fun RegistrationRoute(
    viewModel: RegistrationViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    countryResult: Country?,
    clearCountryResult: () -> Unit,
    onNavigateToCountryPicker: (Country) -> Unit,
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

    LaunchedEffect(countryResult) {
        if (countryResult != null) {
            viewModel.handleEvent(RegistrationReducer.Event.CountrySelected(countryResult))
            clearCountryResult()
        }
    }

    RegistrationScreen(
        state = state, onEvent = viewModel::handleEvent, onCountryCodeClick = {
            onNavigateToCountryPicker(
                Country(
                    dialCode = state.selectedCountryDialCode,
                    code = state.selectedCountryCode,
                    flagEmoji = state.selectedCountryFlag,
                    name = ""
                )
            )
        }, onBackClick = onBackClick
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
                title = stringResource(Res.string.sign_up),
                onBackClick = onBackClick,
                modifier = Modifier.padding(MaterialTheme.spacing.medium)
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

            AppTextField(
                value = state.firstName,
                onValueChange = { onEvent(RegistrationReducer.Event.FirstNameChanged(it)) },
                labelText = stringResource(Res.string.name_label),
                placeholderText = stringResource(Res.string.name_placeholder),
                isError = state.firstNameError != null,
                supportingText = state.firstNameError?.let { stringResource(it) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                )
            )

            AppTextField(
                value = state.surname,
                onValueChange = { onEvent(RegistrationReducer.Event.SurnameChanged(it)) },
                labelText = stringResource(Res.string.surname_label),
                placeholderText = stringResource(Res.string.surname_placeholder),
                isError = state.surnameError != null,
                supportingText = state.surnameError?.let { stringResource(it) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                )
            )

            AppTextField(
                value = state.email,
                onValueChange = { onEvent(RegistrationReducer.Event.EmailChanged(it)) },
                labelText = stringResource(Res.string.email_label),
                isError = state.emailError != null,
                supportingText = state.emailError?.let { stringResource(it) },
                placeholderText = stringResource(Res.string.email_placeholder),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                )
            )

            PhoneTextField(
                value = state.phone,
                onValueChange = { onEvent(RegistrationReducer.Event.PhoneChanged(it)) },
                onCountryCodeClick = onCountryCodeClick,
                countryCode = state.selectedCountryDialCode,
                countryFlag = state.selectedCountryFlag,
                isError = state.phoneError != null,
                supportingText = state.phoneError?.let { stringResource(it) },
                placeholderText = stringResource(Res.string.phone_placeholder),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            AppButton(
                text = stringResource(Res.string.continue_label),
                onClick = { onEvent(RegistrationReducer.Event.Submit) },
                enabled = state.isContinueButtonEnabled,
                isLoading = state.isLoading,
            )
        }
    }
}