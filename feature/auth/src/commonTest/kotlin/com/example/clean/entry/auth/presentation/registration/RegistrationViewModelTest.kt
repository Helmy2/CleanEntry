package com.example.clean.entry.auth.presentation.registration

import app.cash.turbine.test
import com.example.clean.entry.auth.domain.model.AuthMethod
import com.example.clean.entry.auth.domain.repository.AuthRepository
import com.example.clean.entry.auth.domain.usecase.ValidateConfirmPasswordUseCase
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class RegistrationViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockedValidateEmailUseCase: ValidateEmailUseCase
    private lateinit var mockedValidatePhoneUseCase: ValidatePhoneUseCase
    private lateinit var mockedValidatePasswordUseCase: ValidatePasswordUseCase
    private lateinit var mockedValidateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase
    private lateinit var mockedAuthRepository: AuthRepository
    private lateinit var mockedNavigator: AppNavigator
    private lateinit var isAuthenticatedFlow: MutableSharedFlow<Boolean>

    private lateinit var registrationViewModel: RegistrationViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockedValidateEmailUseCase = mock()
        mockedValidatePhoneUseCase = mock()
        mockedValidatePasswordUseCase = mock()
        mockedValidateConfirmPasswordUseCase = mock()
        mockedAuthRepository = mock()
        mockedNavigator = mock()
        isAuthenticatedFlow = MutableSharedFlow(replay = 1)

        every { mockedNavigator.getResultValue<NavigationSavedResult>(any()) } returns emptyFlow()
        every { mockedAuthRepository.isAuthenticated } returns isAuthenticatedFlow

        registrationViewModel = RegistrationViewModel(
            validateEmailUseCase = mockedValidateEmailUseCase,
            validatePhoneUseCase = mockedValidatePhoneUseCase,
            validatePasswordUseCase = mockedValidatePasswordUseCase,
            validateConfirmPasswordUseCase = mockedValidateConfirmPasswordUseCase,
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
            val testRegion = registrationViewModel.state.value.selectedCountry.code
            val mockValidationResult = ValidationResult(isSuccessful = true)

            every { mockedValidatePhoneUseCase(testPhone, testRegion) } returns mockValidationResult

            registrationViewModel.state.test {
                assertEquals(RegistrationReducer.State(), awaitItem())

                registrationViewModel.handleEvent(RegistrationReducer.Event.PhoneChanged(testPhone))

                val updatedState = awaitItem()
                assertEquals(testPhone, updatedState.phone)
                assertNull(updatedState.phoneError)
                assertTrue(updatedState.isContinueButtonEnabled)

                cancelAndConsumeRemainingEvents()
            }

            verify { mockedValidatePhoneUseCase(testPhone, testRegion) }
        }

    @Test
    fun `given email changed event with validation error when handle event then state updated with error`() =
        runTest {
            val testEmail = "invalid-email"
            val errorMsg = "Invalid Email"
            val mockValidationResult =
                ValidationResult(isSuccessful = false, errorMessage = errorMsg)

            every { mockedValidateEmailUseCase(testEmail) } returns mockValidationResult

            registrationViewModel.state.test {
                awaitItem() // initial state

                // Switch to EMAIL auth method to test email logic
                registrationViewModel.handleEvent(
                    RegistrationReducer.Event.AuthMethodChanged(
                        AuthMethod.EMAIL
                    )
                )
                awaitItem()

                registrationViewModel.handleEvent(RegistrationReducer.Event.EmailChanged(testEmail))

                val updatedState = awaitItem()
                assertEquals(testEmail, updatedState.email)
                assertEquals(errorMsg, updatedState.emailError)
                assertFalse(updatedState.isContinueButtonEnabled)

                cancelAndConsumeRemainingEvents()
            }
            verify { mockedValidateEmailUseCase(testEmail) }
        }

    @Test
    fun `given submit with valid phone when verificationId is null then sendVerificationCode is called`() =
        runTest(testDispatcher) {
            val validPhone = "1234567890"
            val expectedVerificationId = "test-verification-id"

            // Setup valid phone state
            every {
                mockedValidatePhoneUseCase(
                    any(),
                    any()
                )
            } returns ValidationResult(isSuccessful = true)
            everySuspend { mockedAuthRepository.sendVerificationCode(any()) } returns Result.success(
                expectedVerificationId
            )
            every { mockedAuthRepository.isAuthenticated } returns flowOf(false)

            registrationViewModel.state.test {

                registrationViewModel.handleEvent(RegistrationReducer.Event.PhoneChanged(validPhone))
                awaitItem() // state after phone change

                registrationViewModel.handleEvent(RegistrationReducer.Event.Submit)

                val finalState = awaitItem()
                assertFalse(finalState.isLoading)
                assertEquals(expectedVerificationId, finalState.verificationId)
            }

            verifySuspend { mockedAuthRepository.sendVerificationCode(any()) }
        }

    @Test
    fun `given CountryButtonClick event then should navigate to CountryCodePicker`() = runTest {
        every { mockedNavigator.navigate(any()) } returns Unit

        val expectedDestination =
            AppDestination.CountryCodePicker(registrationViewModel.state.value.selectedCountry.code)

        registrationViewModel.handleEvent(RegistrationReducer.Event.CountryButtonClick)
        testDispatcher.scheduler.advanceUntilIdle()

        verify { mockedNavigator.navigate(expectedDestination) }
    }

    @Test
    fun `given BackButtonClicked when verificationId is not null then verificationId is cleared`() =
        runTest {
            val verificationId = "test-verification-id"

            registrationViewModel.state.test {
                awaitItem()

                registrationViewModel.handleEvent(
                    RegistrationReducer.Event.VerificationCodeSent(
                        verificationId
                    )
                )
                val otpState = awaitItem()
                assertEquals(verificationId, otpState.verificationId)

                registrationViewModel.handleEvent(RegistrationReducer.Event.BackButtonClicked)

                val finalState = awaitItem()
                assertNull(finalState.verificationId)
            }
        }
}
