package com.example.clean.entry.auth.domain.repository

interface AuthRepository {
    suspend fun sendVerificationCode(phoneNumber: String): Result<String> // Returns verificationId
    suspend fun signInWithPhoneNumber(verificationId: String, code: String): Result<Unit>
    suspend fun registerWithEmailAndPassword(email: String, password: String): Result<Unit>
    suspend fun loginWithEmailAndPassword(email: String, password: String): Result<Unit>
}
