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
import com.example.clean.feature_auth.presentation.login.LoginRoute

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
                        LoginRoute(
                            onNavigateToCountryPicker = {},
                            onLoginSuccess = {},
                            onCreateAccountClick = {}
                        )
                    }
                }
            }
        }
    }
}