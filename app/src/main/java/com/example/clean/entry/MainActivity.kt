package com.example.clean.entry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.clean.entry.design_system.CleanEntryTheme
import com.example.clean.entry.navigation.AppDestination
import com.example.clean.entry.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CleanEntryTheme {
                Scaffold(
                ) {
                    val navController = rememberNavController()
                    AppNavHost(
                        navController = navController,
                        startDestination = AppDestination.AuthGraph,
                        modifier = Modifier.padding(it)
                    )
                }
            }
        }
    }
}