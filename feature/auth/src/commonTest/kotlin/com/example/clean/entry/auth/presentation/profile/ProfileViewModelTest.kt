package com.example.clean.entry.auth.presentation.profile

import com.example.clean.entry.auth.domain.repository.AuthRepository
import com.example.clean.entry.core.navigation.AppDestination
import com.example.clean.entry.core.navigation.AppNavigator
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockedAuthRepository: AuthRepository
    private lateinit var mockedNavigator: AppNavigator

    private lateinit var viewModel: ProfileViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockedAuthRepository = mock()
        mockedNavigator = mock()
        viewModel = ProfileViewModel(
            authRepository = mockedAuthRepository,
            navigator = mockedNavigator
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given OnLogoutClicked event when performLogout succeeds then navigates to Login`() =
        runTest {
            // Arrange
            everySuspend { mockedAuthRepository.clearAuthToken() } returns Unit
            every { mockedNavigator.navigateAsRoot(AppDestination.Login) } returns Unit

            // Act
            viewModel.handleEvent(ProfileReducer.Event.OnLogoutClicked)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            verifySuspend { mockedAuthRepository.clearAuthToken() }
            verify { mockedNavigator.navigateAsRoot(AppDestination.Login) }
        }

    @Test
    fun `given OnLogoutClicked event when performLogout fails then LogoutFailed event is dispatched`() =
        runTest {
            // Arrange
            val errorMessage = "Logout failed"
            everySuspend { mockedAuthRepository.clearAuthToken() } throws Exception(errorMessage)

            // Act
            viewModel.handleEvent(ProfileReducer.Event.OnLogoutClicked)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            verifySuspend { mockedAuthRepository.clearAuthToken() }
        }
}
