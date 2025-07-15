package com.example.clean.entry.feature_auth.presentation.login

import android.content.Context
import app.cash.turbine.test
import com.example.clean.entry.core.domain.model.StringResource
import com.example.clean.entry.core.domain.model.ValidationResult
import com.example.clean.entry.feature_auth.domain.usecase.ValidatePasswordUseCase
import com.example.clean.entry.feature_auth.domain.usecase.ValidatePhoneUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockValidatePhoneUseCase: ValidatePhoneUseCase
    private lateinit var mockValidatePasswordUseCase: ValidatePasswordUseCase

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockValidatePhoneUseCase = mockk()
        mockValidatePasswordUseCase = mockk()

        loginViewModel = LoginViewModel(
            validatePhoneUseCase = mockValidatePhoneUseCase,
            validatePasswordUseCase = mockValidatePasswordUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given phone changed event when handle event then validate phone use case called and state updated`() = runTest {
        val testPhone = "1234567890"
        val testRegion = loginViewModel.state.value.selectedCountryCode
        val mockValidationResult = ValidationResult(isSuccessful = true)

        coEvery { mockValidatePhoneUseCase(testPhone, testRegion) } returns mockValidationResult

        loginViewModel.state.test {
            assertEquals(LoginReducer.State(), awaitItem())

            loginViewModel.handleEvent(LoginReducer.Event.PhoneChanged(testPhone))

            val updatedState = awaitItem()
            assertEquals(testPhone, updatedState.phone)
            assertEquals(mockValidationResult.isSuccessful, updatedState.phoneError == null)

            cancelAndConsumeRemainingEvents()
        }

        coVerify { mockValidatePhoneUseCase(testPhone, testRegion) }
    }

    @Test
    fun `given phone changed event with validation error when handle event then state updated with error message`() = runTest {
        val testPhone = "123"
        val testRegion = loginViewModel.state.value.selectedCountryCode
        val errorMsg = "Invalid phone"
        val mockValidationResult = ValidationResult(
            isSuccessful = false,
            errorMessage = StringResource.FromString(errorMsg)
        )

        coEvery { mockValidatePhoneUseCase(testPhone, testRegion) } returns mockValidationResult

        loginViewModel.state.test {
            assertEquals(LoginReducer.State(), awaitItem())

            loginViewModel.handleEvent(LoginReducer.Event.PhoneChanged(testPhone))

            val updatedState = awaitItem()
            assertEquals(testPhone, updatedState.phone)
            assertNotNull(updatedState.phoneError)
            assertEquals(errorMsg, updatedState.phoneError.asString(mockk(relaxed = true)))
            assertFalse(updatedState.isLoginButtonEnabled)

            cancelAndConsumeRemainingEvents()
        }
        coVerify { mockValidatePhoneUseCase(testPhone, testRegion) }
    }


    @Test
    fun `given password changed event when handle event then validate password use case called and state updated`() = runTest {
        val testPassword = "password123"
        val mockValidationResult = ValidationResult(isSuccessful = true)

        coEvery { mockValidatePasswordUseCase(testPassword) } returns mockValidationResult

        loginViewModel.state.test {
            assertEquals(LoginReducer.State(), awaitItem())

            loginViewModel.handleEvent(LoginReducer.Event.PasswordChanged(testPassword))

            val updatedState = awaitItem()
            assertEquals(testPassword, updatedState.password)
            assertEquals(mockValidationResult.isSuccessful, updatedState.passwordError == null)
            assertFalse(updatedState.isLoginButtonEnabled)

            cancelAndConsumeRemainingEvents()
        }
        coVerify { mockValidatePasswordUseCase(testPassword) }
    }

    @Test
    fun `given password changed event with validation error when handle event then state updated with error message`() = runTest {
        val testPassword = "123"
        val errorMsg = "Password too short"
        val mockValidationResult = ValidationResult(
            isSuccessful = false,
            errorMessage = StringResource.FromString(errorMsg)
        )

        coEvery { mockValidatePasswordUseCase(testPassword) } returns mockValidationResult

        loginViewModel.state.test {
            assertEquals(LoginReducer.State(), awaitItem())

            loginViewModel.handleEvent(LoginReducer.Event.PasswordChanged(testPassword))

            val updatedState = awaitItem()
            assertEquals(testPassword, updatedState.password)
            assertNotNull(updatedState.passwordError)
            assertEquals(errorMsg, updatedState.passwordError.asString(mockk(relaxed = true)))
            assertFalse(updatedState.isLoginButtonEnabled)

            cancelAndConsumeRemainingEvents()
        }

        coVerify { mockValidatePasswordUseCase(testPassword) }
    }


    @Test
    fun `given login clicked event when handle event then state is loading and eventually finished`() = runTest(testDispatcher) {
        val validPhone = "1234567890"
        val validPassword = "password123"
        val phoneRegion = loginViewModel.state.value.selectedCountryCode

        coEvery { mockValidatePhoneUseCase(validPhone, phoneRegion) } returns ValidationResult(isSuccessful = true)
        coEvery { mockValidatePasswordUseCase(validPassword) } returns ValidationResult(isSuccessful = true)

        loginViewModel.handleEvent(LoginReducer.Event.PhoneChanged(validPhone))
        loginViewModel.handleEvent(LoginReducer.Event.PasswordChanged(validPassword))

        loginViewModel.state.test {
            awaitItem() // after phone changed
            val setupState = awaitItem() // after password changed
            assertTrue(setupState.isLoginButtonEnabled)


            loginViewModel.handleEvent(LoginReducer.Event.LoginClicked)

            // State should update to loading = true
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)
            assertFalse(loadingState.isLoginButtonEnabled)


            // State should update to login finished, loading = false
            val finishedState = awaitItem()
            assertFalse(finishedState.isLoading)

            cancelAndConsumeRemainingEvents()
        }
    }

    // Helper for StringResource.asString for testing purposes
    private fun StringResource.asString(context: Context): String {
        return when (this) {
            is StringResource.FromString -> this.value
            is StringResource.FromId -> {
                // In a real test, you'd mock context.getString(id)
                // For simplicity here, just returning the id as a string
                // Or you can use a more sophisticated mock like Roboelectric for context if needed.
                "ResourceId($id)"
            }
        }
    }
}
