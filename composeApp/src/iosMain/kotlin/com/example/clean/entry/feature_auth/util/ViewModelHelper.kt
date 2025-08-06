package com.example.clean.entry.feature_auth.util

import com.example.clean.entry.feature_auth.presentation.login.LoginViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ViewModelHelper: KoinComponent {
    val loginViewModel: LoginViewModel by inject()
}