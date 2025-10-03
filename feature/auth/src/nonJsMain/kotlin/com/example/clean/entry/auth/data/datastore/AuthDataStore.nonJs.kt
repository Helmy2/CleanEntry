package com.example.clean.entry.auth.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

class AuthDataStoreImpl(
    val dataStore: DataStore<Preferences>
) : AuthDataStore {

    private val authTokenKey = stringPreferencesKey("auth_token")

    override val authToken: Flow<String?>
        get() = dataStore.data
            .map { preferences ->
                preferences[authTokenKey]
            }

    override suspend fun saveAuthToken(token: String) {
        dataStore.edit { preferences ->
            preferences[authTokenKey] = token
        }
    }

    override suspend fun clearAuthToken() {
        dataStore.edit { preferences ->
            preferences.remove(authTokenKey)
        }
    }
}

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

internal const val dataStoreFileName = "clean_entry.preferences_pb"