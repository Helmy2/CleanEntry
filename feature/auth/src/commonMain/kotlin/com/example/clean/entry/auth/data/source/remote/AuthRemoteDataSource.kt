package com.example.clean.entry.auth.data.source.remote

import com.example.clean.entry.core.util.Secrets
import com.example.clean.entry.core.util.runCatchingOnIO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable

class AuthRemoteDataSource(private val httpClient: HttpClient) {

    private val apiKey = Secrets.getFirebaseApiKey()
    private val firebaseBaseUrl = "https://identitytoolkit.googleapis.com/v1/accounts"

    suspend fun sendVerificationCode(phoneNumber: String): Result<String> = runCatchingOnIO {
        val response: HttpResponse = httpClient.post {
            url("$firebaseBaseUrl:sendVerificationCode?key=$apiKey")
            contentType(ContentType.Application.Json)
            setBody(SendVerificationCodeRequest(phoneNumber = phoneNumber))
        }

        if (response.status.isSuccess()) {
            response.body<SendVerificationCodeResponse>().sessionInfo
        } else {
            val errorResponse = response.body<FirebaseErrorResponse>()
            throw Exception(errorResponse.error.message)
        }
    }

    suspend fun signInWithPhoneAuth(verificationId: String, code: String): Result<Unit> =
        runCatchingOnIO {
            val response: HttpResponse = httpClient.post {
                url("$firebaseBaseUrl:signInWithPhoneNumber?key=$apiKey")
                contentType(ContentType.Application.Json)
                setBody(SignInWithPhoneNumberRequest(sessionInfo = verificationId, code = code))
            }

            if (response.status.isSuccess()) {
                response.body<SignInWithPhoneNumberResponse>()
                Unit
            } else {
                val errorResponse = response.body<FirebaseErrorResponse>()
                throw Exception(errorResponse.error.message)
            }
        }

    suspend fun registerWithEmailAndPassword(email: String, password: String): Result<Unit> =
        runCatchingOnIO {
            val response: HttpResponse = httpClient.post {
                url("$firebaseBaseUrl:signUp?key=$apiKey")
                contentType(ContentType.Application.Json)
                setBody(
                    EmailPasswordRequest(
                        email = email,
                        password = password,
                        returnSecureToken = true
                    )
                )
            }

            if (response.status.isSuccess()) {
                response.body<EmailPasswordResponse>()
                Unit
            } else {
                val errorResponse = response.body<FirebaseErrorResponse>()
                throw Exception(errorResponse.error.message)
            }
        }

    suspend fun loginWithEmailAndPassword(email: String, password: String): Result<Unit> =
        runCatchingOnIO {
            val response: HttpResponse = httpClient.post {
                url("$firebaseBaseUrl:signInWithPassword?key=$apiKey")
                contentType(ContentType.Application.Json)
                setBody(
                    EmailPasswordRequest(
                        email = email,
                        password = password,
                        returnSecureToken = true
                    )
                )
            }

            if (response.status.isSuccess()) {
                response.body<EmailPasswordResponse>()
                Unit
            } else {
                val errorResponse = response.body<FirebaseErrorResponse>()
                throw Exception(errorResponse.error.message)
            }
        }

    // Request/Response data classes

    @Serializable
    private data class SendVerificationCodeRequest(
        val phoneNumber: String,
        val recaptchaToken: String? = null
    )

    @Serializable
    private data class SendVerificationCodeResponse(val sessionInfo: String)

    @Serializable
    private data class SignInWithPhoneNumberRequest(val sessionInfo: String, val code: String)

    @Serializable
    private data class SignInWithPhoneNumberResponse(
        val idToken: String,
        val refreshToken: String,
        val expiresIn: String,
        val isNewUser: Boolean
    )

    @Serializable
    private data class EmailPasswordRequest(
        val email: String,
        val password: String,
        val returnSecureToken: Boolean
    )

    @Serializable
    private data class EmailPasswordResponse(
        val idToken: String? = null,
        val email: String? = null,
        val refreshToken: String? = null,
        val expiresIn: String? = null,
        val localId: String? = null,
        val registered: Boolean? = null
    )

    // Error handling data classes

    @Serializable
    data class FirebaseErrorResponse(val error: FirebaseError)

    @Serializable
    data class FirebaseError(val code: Int, val message: String)
}
