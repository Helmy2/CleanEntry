package com.example.clean.entry.feature_auth.presentation.country_code_picker

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.clean.entry.core.components.ErrorScreen
import com.example.clean.entry.core.design_system.spacing
import com.example.clean.entry.core.domain.model.Status
import com.example.clean.entry.core.ui.ObserveEffect
import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.presentation.components.CountryRow
import com.example.clean.entry.feature_auth.presentation.components.CountryRowShimmer
import com.example.clean.entry.feature_auth.presentation.country_code_picker.CountryCodePickerReducer.Event.LoadCountries
import com.example.clean.feature_auth.R
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CountryCodePickerRoute(
    viewModel: CountryCodePickerViewModel = koinViewModel(),
    setResult: (Country) -> Unit,
    onNavigateBack: () -> Unit,
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
                setResult(result)
                onNavigateBack()
            }
        }
    }

    CountryCodePickerScreen(
        state = state,
        onEvent = viewModel::handleEvent,
        onBackClick = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryCodePickerScreen(
    state: CountryCodePickerReducer.State,
    onEvent: (CountryCodePickerReducer.Event) -> Unit,
    onBackClick: () -> Unit,
) {
    val countries: LazyPagingItems<Country> = state.filteredCountryFlow.collectAsLazyPagingItems()

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
                        contentDescription = stringResource(R.string.search)
                    )
                },
                singleLine = true,
                placeholder = {
                    Text(
                        text = stringResource(R.string.search),
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
                                contentDescription = stringResource(R.string.back)
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
                            count = countries.itemCount,
                            key = { index -> countries[index]?.code ?: "" }
                        ) { index ->
                            val country = countries[index]
                            if (country != null) {
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
                        }

                        if (countries.loadState.refresh == LoadState.Loading || countries.loadState.append == LoadState.Loading) {
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
