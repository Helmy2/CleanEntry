package com.example.clean.entry.auth.data.datastore

import kotlinx.coroutines.flow.Flow

interface AuthDataStore {
    val authToken: Flow<String?>
    suspend fun saveAuthToken(token: String)
    suspend fun clearAuthToken()
}

