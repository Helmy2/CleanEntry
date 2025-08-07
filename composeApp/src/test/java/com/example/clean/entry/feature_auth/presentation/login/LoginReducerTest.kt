package com.example.clean.entry.feature_auth.presentation.login

import com.example.clean.entry.core.domain.model.StringResource
import com.example.clean.entry.core.domain.model.ValidationResult
import com.example.clean.entry.feature_auth.domain.model.Country
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LoginReducerTest {

    private val initialState = LoginReducer.State()

    @Test
    fun `given PhoneUpdated event when reduce then state phone and phoneError are updated`() {
        val testPhone = "123456789"
        val validationSuccess = ValidationResult(isSuccessful = true)
        val event = LoginReducer.Event.PhoneUpdated(testPhone, validationSuccess)

        val (newState, effect) = LoginReducer.reduce(initialState, event)

        assertEquals(testPhone, newState.phone)
        assertNull(newState.phoneError)
        assertNull(effect)
    }

    @Test
    fun `given PhoneUpdated event with error when reduce then state phone and phoneError are updated with error`() {
        val testPhone = "123"
        val errorMsg = "Invalid phone"
        val validationError = ValidationResult(
            isSuccessful = false,
            errorMessage = StringResource.FromString(errorMsg)
        )
        val event = LoginReducer.Event.PhoneUpdated(testPhone, validationError)

        val (newState, effect) = LoginReducer.reduce(initialState, event)

        assertEquals(testPhone, newState.phone)
        assertEquals(validationError.errorMessage, newState.phoneError)
        assertNull(effect)
    }

    @Test
    fun `given PasswordUpdated event when reduce then state password and passwordError are updated`() {
        val testPassword = "password123"
        val validationSuccess = ValidationResult(isSuccessful = true)
        val event = LoginReducer.Event.PasswordUpdated(testPassword, validationSuccess)

        val (newState, effect) = LoginReducer.reduce(initialState, event)

        assertEquals(testPassword, newState.password)
        assertNull(newState.passwordError)
        assertNull(effect)
    }

    @Test
    fun `given PasswordUpdated event with error when reduce then state password and passwordError are updated with error`() {
        val testPassword = "123"
        val errorMsg = "Password too short"
        val validationError = ValidationResult(
            isSuccessful = false,
            errorMessage = StringResource.FromString(errorMsg)
        )
        val event = LoginReducer.Event.PasswordUpdated(testPassword, validationError)

        val (newState, effect) = LoginReducer.reduce(initialState, event)

        assertEquals(testPassword, newState.password)
        assertEquals(validationError.errorMessage, newState.passwordError)
        assertNull(effect)
    }

    @Test
    fun `given TogglePasswordVisibility event when reduce then isPasswordVisible is toggled`() {
        val (newState, effect) = LoginReducer.reduce(initialState, LoginReducer.Event.TogglePasswordVisibility)

        assertTrue(newState.isPasswordVisible)
        assertNull(effect)
    }

    @Test
    fun `given CountrySelected event when reduce then state selected country details are updated`() {
        val selectedCountry = Country(
            name = "United States",
            dialCode = "+1",
            code = "US",
            flagEmoji = "🇺🇸"
        )
        val event = LoginReducer.Event.CountrySelected(selectedCountry)

        val (newState, effect) = LoginReducer.reduce(initialState, event)

        assertEquals(selectedCountry.code, newState.selectedCountryCode)
        assertEquals(selectedCountry.dialCode, newState.selectedCountryDialCode)
        assertEquals(selectedCountry.flagEmoji, newState.selectedCountryFlag)
        assertNull(effect)
    }

    @Test
    fun `given LoginClicked event when reduce then state isLoading is true`() {
        val event = LoginReducer.Event.LoginClicked

        val (newState, effect) = LoginReducer.reduce(initialState, event)

        assertTrue(newState.isLoading)
        assertNull(effect)
    }

    @Test
    fun `given LoginFinished event when reduce then state isLoading is false and LoginSuccess effect is emitted`() {
        val loadingState = initialState.copy(isLoading = true)
        val event = LoginReducer.Event.LoginFinished

        val (newState, effect) = LoginReducer.reduce(loadingState, event)

        assertFalse(newState.isLoading)
        assertEquals(LoginReducer.Effect.LoginSuccess, effect)
    }

    @Test
    fun `given unhandled event when reduce then previous state is returned with no effect`() {
         val unhandledEvent = LoginReducer.Event.PhoneChanged("some phone")

        val (newState, effect) = LoginReducer.reduce(initialState, unhandledEvent)

        assertEquals(initialState, newState)
        assertNull(effect)
    }

    @Test
    fun `isLoginButtonEnabled should be true when phone and password are valid and not loading`() {
        val state = LoginReducer.State(
            phone = "123456789",
            phoneError = null,
            password = "password123",
            passwordError = null,
            isLoading = false
        )
        assertTrue(state.isLoginButtonEnabled)
    }

    @Test
    fun `isLoginButtonEnabled should be false if phone is blank`() {
        val state = LoginReducer.State(
            phone = "",
            phoneError = null,
            password = "password123",
            passwordError = null,
            isLoading = false
        )
        assertFalse(state.isLoginButtonEnabled)
    }

    @Test
    fun `isLoginButtonEnabled should be false if phone has error`() {
        val state = LoginReducer.State(
            phone = "123",
            phoneError = StringResource.FromString("Error"),
            password = "password123",
            passwordError = null,
            isLoading = false
        )
        assertFalse(state.isLoginButtonEnabled)
    }

    @Test
    fun `isLoginButtonEnabled should be false if password is blank`() {
        val state = LoginReducer.State(
            phone = "123456789",
            phoneError = null,
            password = "",
            passwordError = null,
            isLoading = false
        )
        assertFalse(state.isLoginButtonEnabled)
    }

    @Test
    fun `isLoginButtonEnabled should be false if password has error`() {
        val state = LoginReducer.State(
            phone = "123456789",
            phoneError = null,
            password = "123",
            passwordError = StringResource.FromString("Error"),
            isLoading = false
        )
        assertFalse(state.isLoginButtonEnabled)
    }

    @Test
    fun `isLoginButtonEnabled should be false if isLoading is true`() {
        val state = LoginReducer.State(
            phone = "123456789",
            phoneError = null,
            password = "password123",
            passwordError = null,
            isLoading = true
        )
        assertFalse(state.isLoginButtonEnabled)
    }
}
