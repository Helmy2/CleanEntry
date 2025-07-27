package com.example.clean.entry.feature_auth.presentation.country_code_picker

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.clean.entry.core.components.LoadingScreen
import com.example.clean.entry.core.design_system.spacing
import com.example.clean.entry.core.domain.model.Status
import com.example.clean.entry.core.ui.ObserveEffect
import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.presentation.components.CountryRow
import com.example.clean.entry.feature_auth.presentation.components.TopBarWithBackNavigation
import com.example.clean.entry.feature_auth.presentation.country_code_picker.CountryCodePickerReducer.Event.CountrySelected
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
    val countries: LazyPagingItems<Country> = state.countryFlow.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopBarWithBackNavigation(
                title = stringResource(R.string.select_country),
                onBackClick = onBackClick,
                modifier = Modifier.padding(MaterialTheme.spacing.medium)
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
                        ) { index ->
                            val country = countries[index]
                            if (country != null) {
                                CountryRow(
                                    country = country,
                                    isSelected = state.selectedCountry?.code == country.code,
                                    onClick = { onEvent(CountrySelected(it)) }
                                )
                            }
                        }

                        if (countries.loadState.refresh == LoadState.Loading || countries.loadState.append == LoadState.Loading) {
                            item {
                                CircularProgressIndicator(
                                    modifier =
                                        Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
