package com.example.clean.entry.auth.presentation.login

import app.cash.turbine.test
import com.example.clean.entry.auth.domain.repository.AuthRepository
import com.example.clean.entry.auth.domain.usecase.ValidateEmailUseCase
import com.example.clean.entry.auth.domain.usecase.ValidatePasswordUseCase
import com.example.clean.entry.auth.domain.usecase.ValidatePhoneUseCase
import com.example.clean.entry.core.domain.model.ValidationResult
import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.core.navigation.AppNavigator
import com.example.clean.entry.core.navigation.NavigationSavedResult
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockedValidatePhone: ValidatePhoneUseCase
    private lateinit var mockedPasswordValidator: ValidatePasswordUseCase
    private lateinit var mockedEmailUseCase: ValidateEmailUseCase
    private lateinit var mockedAuthRepository: AuthRepository
    private lateinit var mockedNavigator: AppNavigator
    private lateinit var isAuthenticatedFlow: MutableSharedFlow<Boolean>

    private lateinit var loginViewModel: LoginViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockedValidatePhone = mock()
        mockedPasswordValidator = mock()
        mockedAuthRepository = mock()
        mockedNavigator = mock()
        mockedEmailUseCase = mock()
        isAuthenticatedFlow = MutableSharedFlow(replay = 1)

        every { mockedNavigator.getResultValue<NavigationSavedResult>(any()) } returns emptyFlow()
        every { mockedAuthRepository.isAuthenticated } returns isAuthenticatedFlow

        loginViewModel = LoginViewModel(
            validatePhoneUseCase = mockedValidatePhone,
            validatePasswordUseCase = mockedPasswordValidator,
            validateEmailUseCase = mockedEmailUseCase,
            authRepository = mockedAuthRepository,
            navigator = mockedNavigator
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given phone changed event when handle event then validate phone use case called and state updated`() =
        runTest {
            val testPhone = "1234567890"
            val testRegion = loginViewModel.state.value.selectedCountry.code
            val mockValidationResult = ValidationResult(isSuccessful = true)

            everySuspend { mockedValidatePhone(testPhone, testRegion) } returns mockValidationResult

            loginViewModel.state.test {
                assertEquals(LoginReducer.State(), awaitItem())

                loginViewModel.handleEvent(LoginReducer.Event.PhoneChanged(testPhone))

                val updatedState = awaitItem()
                assertEquals(testPhone, updatedState.phone)
                assertEquals(mockValidationResult.isSuccessful, updatedState.phoneError == null)

                cancelAndConsumeRemainingEvents()
            }

            verifySuspend { mockedValidatePhone(testPhone, testRegion) }
        }

    @Test
    fun `given phone changed event with validation error when handle event then state updated with error message`() =
        runTest {
            val testPhone = "123"
            val testRegion = loginViewModel.state.value.selectedCountry.code
            val errorMsg = "Invalid phone"
            val mockValidationResult = ValidationResult(
                isSuccessful = false,
                errorMessage = errorMsg
            )

            everySuspend { mockedValidatePhone(testPhone, testRegion) } returns mockValidationResult

            loginViewModel.state.test {
                assertEquals(LoginReducer.State(), awaitItem())

                loginViewModel.handleEvent(LoginReducer.Event.PhoneChanged(testPhone))

                val updatedState = awaitItem()
                assertEquals(testPhone, updatedState.phone)
                assertNotNull(updatedState.phoneError)
                assertEquals(errorMsg, updatedState.phoneError)
                assertFalse(updatedState.isLoginButtonEnabled)

                cancelAndConsumeRemainingEvents()
            }
            verifySuspend { mockedValidatePhone(testPhone, testRegion) }
        }


    @Test
    fun `given password changed event when handle event then validate password use case called and state updated`() =
        runTest {
            val testPassword = "password123"
            val mockValidationResult = ValidationResult(isSuccessful = true)

            everySuspend { mockedPasswordValidator(testPassword) } returns mockValidationResult

            loginViewModel.state.test {
                assertEquals(LoginReducer.State(), awaitItem())

                loginViewModel.handleEvent(LoginReducer.Event.PasswordChanged(testPassword))

                val updatedState = awaitItem()
                assertEquals(testPassword, updatedState.password)
                assertEquals(mockValidationResult.isSuccessful, updatedState.passwordError == null)
                assertFalse(updatedState.isLoginButtonEnabled)

                cancelAndConsumeRemainingEvents()
            }
            verifySuspend { mockedPasswordValidator(testPassword) }
        }

    @Test
    fun `given password changed event with validation error when handle event then state updated with error message`() =
        runTest {
            val testPassword = "123"
            val errorMsg = "Password too short"
            val mockValidationResult = ValidationResult(
                isSuccessful = false,
                errorMessage = errorMsg
            )

            everySuspend { mockedPasswordValidator(testPassword) } returns mockValidationResult

            loginViewModel.state.test {
                assertEquals(LoginReducer.State(), awaitItem())

                loginViewModel.handleEvent(LoginReducer.Event.PasswordChanged(testPassword))

                val updatedState = awaitItem()
                assertEquals(testPassword, updatedState.password)
                assertNotNull(updatedState.passwordError)
                assertEquals(errorMsg, updatedState.passwordError)
                assertFalse(updatedState.isLoginButtonEnabled)

                cancelAndConsumeRemainingEvents()
            }

            verifySuspend { mockedPasswordValidator(testPassword) }
        }


    @Test
    fun `given valid inputs when phone and password change then login button is enabled`() =
        runTest(testDispatcher) {
            val validPhone = "1234567890"
            val validPassword = "password123"
            val phoneRegion = loginViewModel.state.value.selectedCountry.code

            everySuspend { mockedValidatePhone(validPhone, phoneRegion) } returns ValidationResult(
                isSuccessful = true
            )
            everySuspend { mockedPasswordValidator(validPassword) } returns ValidationResult(
                isSuccessful = true
            )

            loginViewModel.state.test {
                assertEquals(LoginReducer.State(), awaitItem()) // Initial state

                loginViewModel.handleEvent(LoginReducer.Event.PhoneChanged(validPhone))
                awaitItem() // Consume phone change state

                loginViewModel.handleEvent(LoginReducer.Event.PasswordChanged(validPassword))
                val setupState = awaitItem() // after password changed
                assertTrue(setupState.isLoginButtonEnabled)

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `given submit clicked with valid inputs then send verification code is called`() =
        runTest(testDispatcher) {
            val validPhone = "1234567890"
            val validPassword = "password123"
            val fullPhoneNumber = loginViewModel.state.value.selectedCountry.dialCode + validPhone

            everySuspend {
                mockedValidatePhone(
                    any(),
                    any()
                )
            } returns ValidationResult(isSuccessful = true)
            everySuspend { mockedPasswordValidator(any()) } returns ValidationResult(isSuccessful = true)
            everySuspend { mockedAuthRepository.sendVerificationCode(fullPhoneNumber) } returns Result.success(
                "verificationId"
            )

            loginViewModel.state.test {
                awaitItem()

                loginViewModel.handleEvent(LoginReducer.Event.PhoneChanged(validPhone))
                awaitItem()

                loginViewModel.handleEvent(LoginReducer.Event.PasswordChanged(validPassword))
                awaitItem()

                loginViewModel.handleEvent(LoginReducer.Event.Submit)

                val loadingState = awaitItem()
                assertTrue(loadingState.isLoading)

                val finalState = awaitItem()
                assertFalse(finalState.isLoading)
                assertNotNull(finalState.verificationId)
            }

            verifySuspend { mockedAuthRepository.sendVerificationCode(fullPhoneNumber) }
        }

    @Test
    fun `given submit clicked with valid inputs then state is loading`() =
        runTest(testDispatcher) {
            val validPhone = "1234567890"
            val validPassword = "password123"
            val phoneRegion = loginViewModel.state.value.selectedCountry.code

            everySuspend { mockedValidatePhone(validPhone, phoneRegion) } returns ValidationResult(
                isSuccessful = true
            )
            everySuspend { mockedPasswordValidator(validPassword) } returns ValidationResult(
                isSuccessful = true
            )
            everySuspend { mockedAuthRepository.sendVerificationCode(any()) } returns Result.success(
                "verificationId"
            )

            loginViewModel.state.test {
                loginViewModel.handleEvent(LoginReducer.Event.PhoneChanged(validPhone))
                skipItems(1)
                loginViewModel.handleEvent(LoginReducer.Event.PasswordChanged(validPassword))
                skipItems(1)

                loginViewModel.handleEvent(LoginReducer.Event.Submit)

                val loadingState = awaitItem()
                assertTrue(loadingState.isLoading)

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `given submit clicked with valid inputs then login button is disabled during load`() =
        runTest(testDispatcher) {
            val validPhone = "1234567890"
            val validPassword = "password123"
            val phoneRegion = loginViewModel.state.value.selectedCountry.code

            everySuspend { mockedValidatePhone(validPhone, phoneRegion) } returns ValidationResult(
                isSuccessful = true
            )
            everySuspend { mockedPasswordValidator(validPassword) } returns ValidationResult(
                isSuccessful = true
            )
            everySuspend { mockedAuthRepository.sendVerificationCode(any()) } returns Result.success(
                "verificationId"
            )

            loginViewModel.state.test {
                loginViewModel.handleEvent(LoginReducer.Event.PhoneChanged(validPhone))
                skipItems(1)
                loginViewModel.handleEvent(LoginReducer.Event.PasswordChanged(validPassword))
                skipItems(1)
                loginViewModel.handleEvent(LoginReducer.Event.Submit)
                skipItems(1)

                val loadingState = awaitItem()
                assertFalse(loadingState.isLoginButtonEnabled)

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `given verification code sent successfully then state is not loading`() =
        runTest(testDispatcher) {
            val validPhone = "1234567890"
            val validPassword = "password123"
            val phoneRegion = loginViewModel.state.value.selectedCountry.code
            val fullPhoneNumber = loginViewModel.state.value.selectedCountry.dialCode + validPhone

            everySuspend { mockedValidatePhone(validPhone, phoneRegion) } returns ValidationResult(
                isSuccessful = true
            )
            everySuspend { mockedPasswordValidator(validPassword) } returns ValidationResult(
                isSuccessful = true
            )
            everySuspend { mockedAuthRepository.sendVerificationCode(fullPhoneNumber) } returns Result.success(
                "verificationId"
            )

            loginViewModel.state.test {
                loginViewModel.handleEvent(LoginReducer.Event.PhoneChanged(validPhone))
                skipItems(1)
                loginViewModel.handleEvent(LoginReducer.Event.PasswordChanged(validPassword))
                skipItems(1)
                loginViewModel.handleEvent(LoginReducer.Event.Submit)
                skipItems(1)

                val finishedState = awaitItem()
                assertFalse(finishedState.isLoading)

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `given verification code sent successfully then verificationId is available in state`() =
        runTest(testDispatcher) {
            val validPhone = "1234567890"
            val validPassword = "password123"
            val phoneRegion = loginViewModel.state.value.selectedCountry.code
            val fullPhoneNumber = loginViewModel.state.value.selectedCountry.dialCode + validPhone

            everySuspend { mockedValidatePhone(validPhone, phoneRegion) } returns ValidationResult(
                isSuccessful = true
            )
            everySuspend { mockedPasswordValidator(validPassword) } returns ValidationResult(
                isSuccessful = true
            )
            everySuspend { mockedAuthRepository.sendVerificationCode(fullPhoneNumber) } returns Result.success(
                "verificationId"
            )

            loginViewModel.state.test {
                loginViewModel.handleEvent(LoginReducer.Event.PhoneChanged(validPhone))
                skipItems(1)
                loginViewModel.handleEvent(LoginReducer.Event.PasswordChanged(validPassword))
                skipItems(1)
                loginViewModel.handleEvent(LoginReducer.Event.Submit)
                skipItems(1)

                val finishedState = awaitItem()
                assertNotNull(finishedState.verificationId)

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `given CreateAccountClicked event then should navigate to Registration`() = runTest {
        everySuspend { mockedNavigator.navigate(any()) } returns Unit

        // When
        loginViewModel.handleEvent(LoginReducer.Event.CreateAccountClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify { mockedNavigator.navigate(AppDestination.Registration) }
    }

    @Test
    fun `given CountryButtonClick event then should navigate to CountryCodePicker`() = runTest {
        everySuspend { mockedNavigator.navigate(any()) } returns Unit

        // Given
        val expectedDestination =
            AppDestination.CountryCodePicker(loginViewModel.state.value.selectedCountry.code)

        // When
        loginViewModel.handleEvent(LoginReducer.Event.CountryButtonClick)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify { mockedNavigator.navigate(expectedDestination) }
    }
}
