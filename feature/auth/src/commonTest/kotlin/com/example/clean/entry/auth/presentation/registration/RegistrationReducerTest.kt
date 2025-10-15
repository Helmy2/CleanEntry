package com.example.clean.entry.auth.presentation.registration

import com.example.clean.entry.auth.domain.model.AuthMethod
import com.example.clean.entry.auth.domain.model.Country
import com.example.clean.entry.core.domain.model.ValidationResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RegistrationReducerTest {

    private val reducer = RegistrationReducer

    @Test
    fun `given EmailUpdated event with success then state is updated correctly`() {
        // Arrange
        val previousState = RegistrationReducer.State()
        val email = "test@example.com"
        val validationResult = ValidationResult(isSuccessful = true)
        val event = RegistrationReducer.Event.EmailUpdated(email, validationResult)

        // Act
        val (newState, _) = reducer.reduce(previousState, event)

        // Assert
        assertEquals(email, newState.email)
        assertNull(newState.emailError)
    }

    @Test
    fun `given EmailUpdated event with error then state is updated with error`() {
        // Arrange
        val previousState = RegistrationReducer.State()
        val email = "invalid-email"
        val errorMessage = "Invalid email"
        val validationResult = ValidationResult(isSuccessful = false, errorMessage = errorMessage)
        val event = RegistrationReducer.Event.EmailUpdated(email, validationResult)

        // Act
        val (newState, _) = reducer.reduce(previousState, event)

        // Assert
        assertEquals(email, newState.email)
        assertEquals(errorMessage, newState.emailError)
    }

    @Test
    fun `given PhoneUpdated event with success then state is updated correctly`() {
        // Arrange
        val previousState = RegistrationReducer.State()
        val phone = "1234567890"
        val validationResult = ValidationResult(isSuccessful = true)
        val event = RegistrationReducer.Event.PhoneUpdated(phone, validationResult)

        // Act
        val (newState, _) = reducer.reduce(previousState, event)

        // Assert
        assertEquals(phone, newState.phone)
        assertNull(newState.phoneError)
    }

    @Test
    fun `given PhoneUpdated event with error then state is updated with error`() {
        // Arrange
        val previousState = RegistrationReducer.State()
        val phone = "123"
        val errorMessage = "Invalid phone number"
        val validationResult = ValidationResult(isSuccessful = false, errorMessage = errorMessage)
        val event = RegistrationReducer.Event.PhoneUpdated(phone, validationResult)

        // Act
        val (newState, _) = reducer.reduce(previousState, event)

        // Assert
        assertEquals(phone, newState.phone)
        assertEquals(errorMessage, newState.phoneError)
    }

    @Test
    fun `given PasswordUpdated event with success then state is updated correctly`() {
        // Arrange
        val previousState = RegistrationReducer.State()
        val password = "password123"
        val validationResult = ValidationResult(isSuccessful = true)
        val event = RegistrationReducer.Event.PasswordUpdated(password, validationResult)

        // Act
        val (newState, _) = reducer.reduce(previousState, event)

        // Assert
        assertEquals(password, newState.password)
        assertNull(newState.passwordError)
    }

    @Test
    fun `given ConfirmPasswordUpdated event with success then state is updated correctly`() {
        // Arrange
        val previousState = RegistrationReducer.State()
        val password = "password123"
        val validationResult = ValidationResult(isSuccessful = true)
        val event = RegistrationReducer.Event.ConfirmPasswordUpdated(password, validationResult)

        // Act
        val (newState, _) = reducer.reduce(previousState, event)

        // Assert
        assertEquals(password, newState.confirmPassword)
        assertNull(newState.confirmPasswordError)
    }

    @Test
    fun `given CountrySelected event then state is updated with new country`() {
        // Arrange
        val previousState = RegistrationReducer.State()
        val country = Country.Egypt
        val event = RegistrationReducer.Event.CountrySelected(country)

        // Act
        val (newState, _) = reducer.reduce(previousState, event)

        // Assert
        assertEquals(country, newState.selectedCountry)
    }

    @Test
    fun `given Submit event then state isLoading is true`() {
        // Arrange
        val previousState = RegistrationReducer.State()
        val event = RegistrationReducer.Event.Submit

        // Act
        val (newState, _) = reducer.reduce(previousState, event)

        // Assert
        assertTrue(newState.isLoading)
        assertNull(newState.error)
    }

    @Test
    fun `given RegistrationSuccess event then state is reset`() {
        // Arrange
        val previousState = RegistrationReducer.State(
            email = "test@test.com",
            isLoading = true,
            error = "some error"
        )
        val event = RegistrationReducer.Event.RegistrationSuccess

        // Act
        val (newState, _) = reducer.reduce(previousState, event)

        // Assert
        assertEquals(RegistrationReducer.State(), newState)
    }

    @Test
    fun `given RegistrationFailed event then state is updated with error and isLoading is false`() {
        // Arrange
        val previousState = RegistrationReducer.State(isLoading = true)
        val errorMessage = "Registration failed"
        val event = RegistrationReducer.Event.RegistrationFailed(errorMessage)

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
        val previousState = RegistrationReducer.State()
        val authMethod = AuthMethod.EMAIL
        val event = RegistrationReducer.Event.AuthMethodChanged(authMethod)

        // Act
        val (newState, _) = reducer.reduce(previousState, event)

        // Assert
        assertEquals(authMethod, newState.authMethod)
        assertNull(newState.error)
    }

    @Test
    fun `given VerificationCodeSent event then state is updated with verificationId`() {
        // Arrange
        val previousState = RegistrationReducer.State(isLoading = true)
        val verificationId = "test-verification-id"
        val event = RegistrationReducer.Event.VerificationCodeSent(verificationId)

        // Act
        val (newState, _) = reducer.reduce(previousState, event)

        // Assert
        assertFalse(newState.isLoading)
        assertEquals(verificationId, newState.verificationId)
    }

    @Test
    fun `given BackButtonClicked event (represented as VerificationCodeSent with null) then state clears verificationId`() {
        // Arrange
        val previousState = RegistrationReducer.State(verificationId = "some-id")
        val event = RegistrationReducer.Event.VerificationCodeSent(null)

        // Act
        val (newState, _) = reducer.reduce(previousState, event)

        // Assert
        assertNull(newState.verificationId)
    }

    @Test
    fun `given OtpChanged event then state is updated with new otp`() {
        // Arrange
        val previousState = RegistrationReducer.State()
        val otp = "123456"
        val event = RegistrationReducer.Event.OtpChanged(otp)

        // Act
        val (newState, _) = reducer.reduce(previousState, event)

        // Assert
        assertEquals(otp, newState.otp)
    }

    @Test
    fun `isContinueButtonEnabled should be true when phone is valid`() {
        val state = RegistrationReducer.State(
            phone = "1234567890",
            phoneError = null,
            authMethod = AuthMethod.PHONE
        )
        assertTrue(state.isContinueButtonEnabled)
    }

    @Test
    fun `isContinueButtonEnabled should be false when phone is invalid`() {
        val state = RegistrationReducer.State(
            phone = "123",
            phoneError = "error",
            authMethod = AuthMethod.PHONE
        )
        assertFalse(state.isContinueButtonEnabled)
    }

    @Test
    fun `isContinueButtonEnabled should be true when email and passwords are valid`() {
        val state = RegistrationReducer.State(
            email = "test@test.com",
            emailError = null,
            password = "password",
            passwordError = null,
            confirmPassword = "password",
            confirmPasswordError = null,
            authMethod = AuthMethod.EMAIL
        )
        assertTrue(state.isContinueButtonEnabled)
    }

    @Test
    fun `isContinueButtonEnabled should be false when email is invalid`() {
        val state = RegistrationReducer.State(
            email = "test",
            emailError = "error",
            password = "password",
            passwordError = null,
            confirmPassword = "password",
            confirmPasswordError = null,
            authMethod = AuthMethod.EMAIL
        )
        assertFalse(state.isContinueButtonEnabled)
    }

    @Test
    fun `isContinueButtonEnabled should be true when OTP is complete`() {
        val state = RegistrationReducer.State(
            verificationId = "some-id",
            otp = "123456",
            otpCount = 6
        )
        assertTrue(state.isContinueButtonEnabled)
    }

    @Test
    fun `isContinueButtonEnabled should be false when OTP is incomplete`() {
        val state = RegistrationReducer.State(
            verificationId = "some-id",
            otp = "123",
            otpCount = 6
        )
        assertFalse(state.isContinueButtonEnabled)
    }

    @Test
    fun `isContinueButtonEnabled should be false when loading`() {
        val state = RegistrationReducer.State(isLoading = true)
        assertFalse(state.isContinueButtonEnabled)
    }
}
