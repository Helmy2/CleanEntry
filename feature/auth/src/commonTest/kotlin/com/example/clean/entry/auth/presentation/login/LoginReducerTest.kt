package com.example.clean.entry.auth.presentation.login

import com.example.clean.entry.auth.domain.model.AuthMethod
import com.example.clean.entry.auth.domain.model.Country
import com.example.clean.entry.core.domain.model.ValidationResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LoginReducerTest {

    private val reducer = LoginReducer
    private val initialState = LoginReducer.State()

    @Test
    fun `given EmailUpdated event with success then state is updated correctly`() {
        // Arrange
        val email = "test@example.com"
        val validationResult = ValidationResult(isSuccessful = true)
        val event = LoginReducer.Event.EmailUpdated(email, validationResult)

        // Act
        val (newState, _) = reducer.reduce(initialState, event)

        // Assert
        assertEquals(email, newState.email)
        assertNull(newState.emailError)
    }

    @Test
    fun `given PhoneUpdated event with error then state is updated with error`() {
        // Arrange
        val phone = "123"
        val errorMessage = "Invalid phone"
        val validationResult = ValidationResult(isSuccessful = false, errorMessage = errorMessage)
        val event = LoginReducer.Event.PhoneUpdated(phone, validationResult)

        // Act
        val (newState, _) = reducer.reduce(initialState, event)

        // Assert
        assertEquals(phone, newState.phone)
        assertEquals(errorMessage, newState.phoneError)
    }

    @Test
    fun `given PasswordUpdated event with success then state is updated correctly`() {
        // Arrange
        val password = "password123"
        val validationResult = ValidationResult(isSuccessful = true)
        val event = LoginReducer.Event.PasswordUpdated(password, validationResult)

        // Act
        val (newState, _) = reducer.reduce(initialState, event)

        // Assert
        assertEquals(password, newState.password)
        assertNull(newState.passwordError)
    }

    @Test
    fun `given TogglePasswordVisibility event then isPasswordVisible is toggled`() {
        // Act
        val (stateAfterFirstToggle, _) = reducer.reduce(
            initialState,
            LoginReducer.Event.TogglePasswordVisibility
        )
        val (stateAfterSecondToggle, _) = reducer.reduce(
            stateAfterFirstToggle,
            LoginReducer.Event.TogglePasswordVisibility
        )

        // Assert
        assertTrue(stateAfterFirstToggle.isPasswordVisible)
        assertFalse(stateAfterSecondToggle.isPasswordVisible)
    }

    @Test
    fun `given CountrySelected event then state is updated with new country`() {
        // Arrange
        val country = Country.Egypt
        val event = LoginReducer.Event.CountrySelected(country)

        // Act
        val (newState, _) = reducer.reduce(initialState, event)

        // Assert
        assertEquals(country, newState.selectedCountry)
    }

    @Test
    fun `given Submit event then state isLoading is true`() {
        // Act
        val (newState, _) = reducer.reduce(initialState, LoginReducer.Event.Submit)

        // Assert
        assertTrue(newState.isLoading)
        assertNull(newState.error)
    }

    @Test
    fun `given LoginSuccess event then state is reset`() {
        // Arrange
        val previousState =
            LoginReducer.State(email = "test@test.com", isLoading = true, error = "error")

        // Act
        val (newState, _) = reducer.reduce(previousState, LoginReducer.Event.LoginSuccess)

        // Assert
        assertEquals(initialState, newState)
    }

    @Test
    fun `given LoginFailed event then state is updated with error and isLoading is false`() {
        // Arrange
        val previousState = LoginReducer.State(isLoading = true)
        val errorMessage = "Login failed"
        val event = LoginReducer.Event.LoginFailed(errorMessage)

        // Act
        val (newState, _) = reducer.reduce(previousState, event)

        // Assert
        assertFalse(newState.isLoading)
        assertEquals(errorMessage, newState.error)
        assertEquals("", newState.otp)
    }

    @Test
    fun `given AuthMethodChanged event then state is updated with new auth method`() {
        // Arrange
        val authMethod = AuthMethod.EMAIL
        val event = LoginReducer.Event.AuthMethodChanged(authMethod)

        // Act
        val (newState, _) = reducer.reduce(initialState, event)

        // Assert
        assertEquals(authMethod, newState.authMethod)
        assertNull(newState.error)
    }

    @Test
    fun `given VerificationCodeSent event then state is updated with verificationId`() {
        // Arrange
        val previousState = LoginReducer.State(isLoading = true)
        val verificationId = "test-verification-id"
        val event = LoginReducer.Event.VerificationCodeSent(verificationId)

        // Act
        val (newState, _) = reducer.reduce(previousState, event)

        // Assert
        assertFalse(newState.isLoading)
        assertEquals(verificationId, newState.verificationId)
    }

    @Test
    fun `given OtpChanged event then state is updated with new otp`() {
        // Arrange
        val otp = "123456"
        val event = LoginReducer.Event.OtpChanged(otp)

        // Act
        val (newState, _) = reducer.reduce(initialState, event)

        // Assert
        assertEquals(otp, newState.otp)
    }

    @Test
    fun `isLoginButtonEnabled should be true for valid phone and password`() {
        val state = LoginReducer.State(
            phone = "1234567890",
            password = "password123",
            authMethod = AuthMethod.PHONE
        )
        assertTrue(state.isLoginButtonEnabled)
    }

    @Test
    fun `isLoginButtonEnabled should be true for valid email and password`() {
        val state = LoginReducer.State(
            email = "test@test.com",
            password = "password123",
            authMethod = AuthMethod.EMAIL
        )
        assertTrue(state.isLoginButtonEnabled)
    }

    @Test
    fun `isLoginButtonEnabled should be true for complete OTP`() {
        val state = LoginReducer.State(verificationId = "id", otp = "123456", otpCount = 6)
        assertTrue(state.isLoginButtonEnabled)
    }

    @Test
    fun `isLoginButtonEnabled should be false when loading`() {
        val state = LoginReducer.State(isLoading = true)
        assertFalse(state.isLoginButtonEnabled)
    }

    @Test
    fun `isLoginButtonEnabled should be false for invalid phone`() {
        val state = LoginReducer.State(
            phone = "1",
            phoneError = "error",
            password = "password123",
            authMethod = AuthMethod.PHONE
        )
        assertFalse(state.isLoginButtonEnabled)
    }

    @Test
    fun `isLoginButtonEnabled should be false for incomplete OTP`() {
        val state = LoginReducer.State(verificationId = "id", otp = "123", otpCount = 6)
        assertFalse(state.isLoginButtonEnabled)
    }
}