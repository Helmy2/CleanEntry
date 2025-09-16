package com.example.clean.entry.auth.presentation.country_code_picker

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
import cleanentry.feature.auth.generated.resources.Res
import cleanentry.feature.auth.generated.resources.back
import cleanentry.feature.auth.generated.resources.search
import com.example.clean.entry.auth.domain.model.Country
import com.example.clean.entry.auth.presentation.components.CountryRow
import com.example.clean.entry.auth.presentation.components.CountryRowShimmer
import com.example.clean.entry.auth.presentation.country_code_picker.CountryCodePickerReducer.Event.LoadCountries
import com.example.clean.entry.core.components.ErrorScreen
import com.example.clean.entry.core.design_system.spacing
import com.example.clean.entry.core.domain.model.Status
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CountryCodePickerRoute(
    viewModel: CountryCodePickerViewModel = koinViewModel(),
    countryCode: String?,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(countryCode) {
        countryCode?.let {
            viewModel.handleEvent(CountryCodePickerReducer.Event.InitCountrySelectedCode(it))
        }
    }

    CountryCodePickerScreen(
        state = state,
        onEvent = viewModel::handleEvent,
    )
}

@Composable
fun CountryCodePickerScreen(
    state: CountryCodePickerReducer.State,
    onEvent: (CountryCodePickerReducer.Event) -> Unit,
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
                        onClick = { onEvent(CountryCodePickerReducer.Event.BackButtonClicked) },
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
//
//@Composable
//fun CountryRow(
//    country: Country,
//    isSelected: Boolean,
//    onClick: (Country) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Column {
//        Row(
//            modifier = modifier
//                .fillMaxWidth()
//                .clickable(onClick = { onClick(country) })
//                .padding(MaterialTheme.spacing.small),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                "👋 \\uD83D\\uDE00",
//                style = MaterialTheme.typography.titleLarge,
//            )
//            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
//            Text(
//                text = country.code,
//                style = MaterialTheme.typography.bodyLarge,
//                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
//            )
//            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
//            Text(
//                text = country.name,
//                style = MaterialTheme.typography.bodyLarge,
//                color = MaterialTheme.colorScheme.onSurface
//            )
//            Spacer(modifier = Modifier.weight(1f))
//            if (isSelected) {
//                Icon(
//                    imageVector = Icons.Default.CheckCircle,
//                    contentDescription = stringResource(Res.string.selected),
//                    tint = MaterialTheme.colorScheme.primary
//                )
//            }
//        }
//        HorizontalDivider()
//    }
//}
