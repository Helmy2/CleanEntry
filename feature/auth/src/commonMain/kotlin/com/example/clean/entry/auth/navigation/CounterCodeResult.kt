package com.example.clean.entry.auth.navigation

import com.example.clean.entry.auth.domain.model.Country
import com.example.clean.entry.core.navigation.NavigationSavedResult

data class CounterCodeResult(val country: Country) : NavigationSavedResult("COUNTER_CODE_KEY") {
    companion object Companion {
        const val KEY = "COUNTER_CODE_KEY"
    }
}