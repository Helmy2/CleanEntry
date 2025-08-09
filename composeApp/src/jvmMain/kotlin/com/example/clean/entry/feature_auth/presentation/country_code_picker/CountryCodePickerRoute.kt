package com.example.clean.entry.feature_auth.presentation.country_code_picker

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cleanentry.composeapp.generated.resources.Res
import cleanentry.composeapp.generated.resources.back
import cleanentry.composeapp.generated.resources.search
import com.example.clean.entry.core.components.ErrorScreen
import com.example.clean.entry.core.design_system.spacing
import com.example.clean.entry.core.domain.model.Status
import com.example.clean.entry.core.ui.ObserveEffect
import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.presentation.components.CountryRow
import com.example.clean.entry.feature_auth.presentation.components.CountryRowShimmer
import com.example.clean.entry.feature_auth.presentation.country_code_picker.CountryCodePickerReducer.Event.LoadCountries
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CountryCodePickerRoute(
    viewModel: CountryCodePickerViewModel = koinViewModel(),
    countryResult: Country?,
    onNavigateBack: (Country?) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ObserveEffect(viewModel.effect) { effect ->
        when (effect) {
            is CountryCodePickerReducer.Effect.NavigateBackWithResult -> {
                val result = Country(
                    dialCode = effect.country.dialCode,
                    code = effect.country.code,
                    flagEmoji = effect.country.flagEmoji,
                    name = effect.country.name
                )
                onNavigateBack(result)
            }
        }
    }

    LaunchedEffect(countryResult) {
        countryResult?.let {
            viewModel.handleEvent(CountryCodePickerReducer.Event.InitCountrySelectedCode(it.code))
        }
    }

    CountryCodePickerScreen(
        state = state,
        onEvent = viewModel::handleEvent,
        onBackClick = { onNavigateBack(null) }
    )
}

@Composable
fun CountryCodePickerScreen(
    state: CountryCodePickerReducer.State,
    onEvent: (CountryCodePickerReducer.Event) -> Unit,
    onBackClick: () -> Unit,
) {
    val countries: List<Country> by state.countryFlow.collectAsStateWithLifecycle(emptyList())

    Scaffold(
        topBar = {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = {
                    onEvent(CountryCodePickerReducer.Event.SearchQueryChanged(it))
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(Res.string.search)
                    )
                },
                singleLine = true,
                placeholder = {
                    Text(
                        text = stringResource(Res.string.search),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical = MaterialTheme.spacing.small,
                    ),
                leadingIcon = {
                    IconButton(
                        onClick = onBackClick,
                        content = {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = stringResource(Res.string.back)
                            )
                        },
                    )
                }
            )
        }
    ) { padding ->
        AnimatedContent(
            state.status,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.medium
                )
        ) { status ->
            when (status) {
                is Status.Error -> ErrorScreen(
                    message = status.message,
                    onRetry = { onEvent(LoadCountries) }
                )

                Status.Idle, Status.Loading -> {
                    LazyColumn {
                        items(
                            count = countries.size,
                            key = { index -> countries[index].code }
                        ) { index ->
                            val country = countries[index]
                            CountryRow(
                                country = country,
                                isSelected = state.selectedCountryCode == country.code,
                                onClick = {
                                    onEvent(
                                        CountryCodePickerReducer.Event.CountrySelectedCode(
                                            it.code
                                        )
                                    )
                                }, modifier = Modifier.padding(
                                    top = MaterialTheme.spacing.small,
                                    bottom = MaterialTheme.spacing.small
                                )
                            )
                        }

                        when {
                            countries.isEmpty() -> {
                                items(10) {
                                    CountryRowShimmer(
                                        modifier = Modifier.padding(
                                            top = MaterialTheme.spacing.small,
                                            bottom = MaterialTheme.spacing.small
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
