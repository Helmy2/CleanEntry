package com.example.clean.entry.auth.data.repository

import com.example.clean.entry.auth.data.datastore.AuthDataStore
import com.example.clean.entry.auth.data.source.remote.AuthRemoteDataSource
import com.example.clean.entry.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val authDataStore: AuthDataStore
) : AuthRepository {

    override val authToken: Flow<String?> = authDataStore.authToken

    override val isAuthenticated: Flow<Boolean> = authDataStore.authToken.map { it != null }

    override suspend fun saveAuthToken(token: String) {
        authDataStore.saveAuthToken(token)
    }

    override suspend fun clearAuthToken() {
        authDataStore.clearAuthToken()
    }

    override suspend fun sendVerificationCode(phoneNumber: String): Result<String> {
        return remoteDataSource.sendVerificationCode(phoneNumber)
    }

    override suspend fun signInWithPhoneNumber(
        verificationId: String,
        code: String
    ): Result<String> {
        return remoteDataSource.signInWithPhoneNumber(verificationId, code).onSuccess { token ->
            saveAuthToken(token)
        }
    }

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Result<String> {
        return remoteDataSource.registerWithEmailAndPassword(email, password).onSuccess { token ->
            saveAuthToken(token)
        }
    }

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Result<String> {
        return remoteDataSource.loginWithEmailAndPassword(email, password).onSuccess { token ->
            saveAuthToken(token)
        }
    }
}