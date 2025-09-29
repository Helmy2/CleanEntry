package com.example.clean.entry.auth.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authToken: Flow<String?>
    val isAuthenticated: Flow<Boolean>
    suspend fun saveAuthToken(token: String)
    suspend fun clearAuthToken()
    suspend fun sendVerificationCode(phoneNumber: String): Result<String>
    suspend fun signInWithPhoneNumber(verificationId: String, code: String): Result<String>
    suspend fun registerWithEmailAndPassword(email: String, password: String): Result<String>
    suspend fun loginWithEmailAndPassword(email: String, password: String): Result<String>
}
