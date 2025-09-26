package com.example.clean.entry.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clean.entry.auth.domain.repository.AuthRepository
import com.example.clean.entry.core.navigation.AppDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel(
    val authRepository: AuthRepository
) : ViewModel() {
    private val _startDestination: MutableStateFlow<Result<AppDestination>?> =
        MutableStateFlow(null)

    val startDestination: StateFlow<Result<AppDestination>?> get() = _startDestination

    init {
        viewModelScope.launch {
            val isAuthenticated = authRepository.isAuthenticated.first()
            _startDestination.value = if (isAuthenticated) {
                Result.success(AppDestination.Dashboard)
            } else {
                Result.success(AppDestination.Auth)
            }
        }
    }
}