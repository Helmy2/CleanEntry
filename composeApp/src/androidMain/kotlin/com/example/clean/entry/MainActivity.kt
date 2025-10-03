package com.example.clean.entry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.clean.entry.core.design_system.CleanEntryTheme
import com.example.clean.entry.core.navigation.AppNavigator
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val splashScreen = installSplashScreen()
        val navigator: AppNavigator by inject()
        setContent {
            val state by navigator.initialDestination.collectAsStateWithLifecycle(null)
            SideEffect {
                splashScreen.setKeepOnScreenCondition {
                    state == null
                }
            }
            CleanEntryTheme {
                if (state != null) {
                    App(state!!)
                }
            }
        }
    }
}