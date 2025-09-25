package com.example.clean.entry.auth.data.repository

import com.example.clean.entry.auth.data.source.remote.AuthRemoteDataSource
import com.example.clean.entry.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun sendVerificationCode(phoneNumber: String): Result<String> {
        return remoteDataSource.sendVerificationCode(phoneNumber)
    }

    override suspend fun signInWithPhoneNumber(verificationId: String, code: String): Result<Unit> {
        return remoteDataSource.signInWithPhoneAuth(verificationId, code)
    }

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Unit> {
        return remoteDataSource.registerWithEmailAndPassword(email, password)
    }

    override suspend fun loginWithEmailAndPassword(email: String, password: String): Result<Unit> {
        return remoteDataSource.loginWithEmailAndPassword(email, password)
    }
}
