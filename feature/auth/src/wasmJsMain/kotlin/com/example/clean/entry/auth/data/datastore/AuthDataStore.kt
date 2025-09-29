package com.example.clean.entry.auth.data.datastore

import kotlinx.browser.localStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthDataStoreImpl : AuthDataStore {

    private val authTokenKey = "auth_token"

    private val _authToken = MutableStateFlow(localStorage.getItem(authTokenKey))
    override val authToken: Flow<String?> = _authToken.asStateFlow()

    override suspend fun saveAuthToken(token: String) {
        localStorage.setItem(authTokenKey, token)
        _authToken.value = token
    }

    override suspend fun clearAuthToken() {
        localStorage.removeItem(authTokenKey)
        _authToken.value = null
    }
}