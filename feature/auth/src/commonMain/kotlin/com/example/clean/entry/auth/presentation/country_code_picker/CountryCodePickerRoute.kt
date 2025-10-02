package com.example.clean.entry.auth.presentation.country_code_picker

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cleanentry.feature.auth.generated.resources.Res
import cleanentry.feature.auth.generated.resources.back
import cleanentry.feature.auth.generated.resources.search
import com.example.clean.entry.auth.presentation.components.CountryRow
import com.example.clean.entry.auth.presentation.components.CountryRowShimmer
import com.example.clean.entry.auth.presentation.country_code_picker.CountryCodePickerReducer.Event.LoadCountries
import com.example.clean.entry.core.components.ErrorScreen
import com.example.clean.entry.core.design_system.spacing
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CountryCodePickerRoute(
    countryCode: String?,
    viewModel: CountryCodePickerViewModel = koinViewModel {
        parametersOf(countryCode)
    },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CountryCodePickerScreen(
        state = state,
        onEvent = viewModel::handleEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryCodePickerScreen(
    state: CountryCodePickerReducer.State,
    onEvent: (CountryCodePickerReducer.Event) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { onEvent(CountryCodePickerReducer.Event.BackButtonClicked) },
                        content = {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = stringResource(Res.string.back)
                            )
                        },
                    )
                },
                title = {
                    OutlinedTextField(
                        value = state.searchQuery,
                        shape = CardDefaults.shape,
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
                            .padding(horizontal = MaterialTheme.spacing.medium),
                    )
                }
            )
        }
    ) { padding ->
        AnimatedContent(
            state.errorMessage,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.medium
                )
        ) { errorMessage ->
            when {
                errorMessage != null -> ErrorScreen(
                    message = errorMessage,
                    onRetry = { onEvent(LoadCountries) }
                )

                else -> {
                    LazyColumn {
                        items(
                            count = state.countries.size,
                            key = { index -> state.countries[index].code }
                        ) { index ->
                            val country = state.countries[index]
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
                            state.countries.isEmpty() -> {
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
