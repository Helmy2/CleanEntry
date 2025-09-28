package com.example.clean.entry.auth.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.clean.entry.auth.presentation.components.TopBar
import com.example.clean.entry.core.design_system.spacing
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileScreen(
        state = state,
        onEvent = viewModel::handleEvent
    )
}

@Composable
fun ProfileScreen(
    state: ProfileReducer.State,
    onEvent: (ProfileReducer.Event) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Profile"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(onClick = { onEvent(ProfileReducer.Event.OnLogoutClicked) }) {
                    Text("Logout")
                }
            }

            state.error?.let {
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
