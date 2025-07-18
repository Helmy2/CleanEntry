package com.example.clean.entry.feature_auth.presentation.country_code_picker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.clean.entry.core.design_system.spacing
import com.example.clean.entry.core.ui.ObserveEffect
import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.presentation.components.CountryRow
import com.example.clean.entry.feature_auth.presentation.components.TopBarWithBackNavigation
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
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBarWithBackNavigation(
                title = stringResource(R.string.select_country),
                onBackClick = onBackClick,
                modifier = Modifier.padding(MaterialTheme.spacing.medium)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

            } else {
                LazyColumn {
                    items(state.countries) { country ->
                        CountryRow(
                            countryFlag = country.flagEmoji,
                            countryCode = country.dialCode,
                            countryName = country.name,
                            isSelected = state.selectedCountry?.code == country.code,
                            onClick = {
                                onEvent(
                                    CountryCodePickerReducer.Event.CountrySelected(
                                        country
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
