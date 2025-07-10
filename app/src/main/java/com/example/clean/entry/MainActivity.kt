package com.example.clean.entry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.clean.core.design_system.CleanEntryTheme
import com.example.clean.entry.feature_auth.domain.model.Country
import com.example.clean.entry.feature_auth.presentation.country_code_picker.CountryCodePickerRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CleanEntryTheme {
                Scaffold {
                    Box(
                        modifier = Modifier.padding(it)
                    ) {
                        CountryCodePickerRoute(
                            onCountrySelected = { dialCode, code -> },
                            onNavigateBack = {},
                            viewModel = koinViewModel {
                                parametersOf(
                                    Country(
                                        dialCode = "EG",
                                        code = "EG",
                                        flagEmoji = "🇪🇬",
                                        name = "Egypt"
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