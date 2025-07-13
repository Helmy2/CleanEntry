package com.example.clean.entry.feature_auth.presentation.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.clean.entry.core.design_system.CleanEntryTheme
import com.example.clean.entry.core.domain.model.StringResource
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertTrue


@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockOnEvent: (LoginReducer.Event) -> Unit = mockk(relaxed = true)
    private val mockOnCountryCodeClick: () -> Unit = mockk(relaxed = true)
    private val mockOnCreateAccountClick: () -> Unit = mockk(relaxed = true)

    @Test
    fun loginScreen_whenPhoneAndPasswordProvided_continueButtonIsEnabled() {
        val state = LoginReducer.State(phone = "1234567890", password = "password123")
        assertTrue(state.isLoginButtonEnabled)

        composeTestRule.setContent {
            CleanEntryTheme {
                LoginScreen(
                    state = state,
                    onEvent = mockOnEvent,
                    onCountryCodeClick = mockOnCountryCodeClick,
                    onCreateAccountClick = mockOnCreateAccountClick
                )
            }
        }
        composeTestRule.onNodeWithTag("login_button").assertIsEnabled()
    }

    @Test
    fun loginScreen_whenLoading_continueButtonShowsLoadingAndIsDisabled() {
        val state =
            LoginReducer.State(phone = "1234567890", password = "password123", isLoading = true)

        composeTestRule.setContent {
            CleanEntryTheme {
                LoginScreen(
                    state = state,
                    onEvent = mockOnEvent,
                    onCountryCodeClick = mockOnCountryCodeClick,
                    onCreateAccountClick = mockOnCreateAccountClick
                )
            }
        }

        composeTestRule.onNodeWithTag("login_button").assertIsNotEnabled()
    }

    @Test
    fun loginScreen_phoneInput_triggersPhoneChangedEvent() {
        val initialState = LoginReducer.State()
        composeTestRule.setContent {
            CleanEntryTheme {
                LoginScreen(
                    state = initialState,
                    onEvent = mockOnEvent,
                    onCountryCodeClick = mockOnCountryCodeClick,
                    onCreateAccountClick = mockOnCreateAccountClick
                )
            }
        }

        val phoneInput = "12345"
        composeTestRule.onNodeWithText("5xxxxxxx").performTextInput(phoneInput)
        verify { mockOnEvent(LoginReducer.Event.PhoneChanged(phoneInput)) }
    }

    @Test
    fun loginScreen_clickContinueButton_triggersLoginClickedEvent() {
        val state = LoginReducer.State(phone = "1234567890", password = "password123")
        composeTestRule.setContent {
            CleanEntryTheme {
                LoginScreen(
                    state = state,
                    onEvent = mockOnEvent,
                    onCountryCodeClick = mockOnCountryCodeClick,
                    onCreateAccountClick = mockOnCreateAccountClick
                )
            }
        }

        composeTestRule.onNodeWithTag("login_button").performClick()
        verify { mockOnEvent(LoginReducer.Event.LoginClicked) }
    }

    @Test
    fun loginScreen_clickCreateAccount_triggersCreateAccountCallback() {
        val initialState = LoginReducer.State()
        composeTestRule.setContent {
            CleanEntryTheme {
                LoginScreen(
                    state = initialState,
                    onEvent = mockOnEvent,
                    onCountryCodeClick = mockOnCountryCodeClick,
                    onCreateAccountClick = mockOnCreateAccountClick
                )
            }
        }

        composeTestRule.onNodeWithText("Create account").performClick()
        verify { mockOnCreateAccountClick() }
    }

    @Test
    fun loginScreen_clickCountryCode_triggersCountryCodeClickCallback() {
        val initialState = LoginReducer.State()
        composeTestRule.setContent {
            CleanEntryTheme {
                LoginScreen(
                    state = initialState,
                    onEvent = mockOnEvent,
                    onCountryCodeClick = mockOnCountryCodeClick,
                    onCreateAccountClick = mockOnCreateAccountClick
                )
            }
        }

        composeTestRule.onNodeWithText(initialState.selectedCountryDialCode).performClick()
        verify { mockOnCountryCodeClick() }
    }

    @Test
    fun loginScreen_phoneError_displaysErrorMessage() {
        val errorMessage = "Invalid phone number"
        val state = LoginReducer.State(phoneError = StringResource.FromString(errorMessage))
        composeTestRule.setContent {
            CleanEntryTheme {
                LoginScreen(
                    state = state,
                    onEvent = mockOnEvent,
                    onCountryCodeClick = mockOnCountryCodeClick,
                    onCreateAccountClick = mockOnCreateAccountClick
                )
            }
        }
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun loginScreen_passwordError_displaysErrorMessage() {
        val errorMessage = "Password too short"
        val state = LoginReducer.State(passwordError = StringResource.FromString(errorMessage))
        composeTestRule.setContent {
            CleanEntryTheme {
                LoginScreen(
                    state = state,
                    onEvent = mockOnEvent,
                    onCountryCodeClick = mockOnCountryCodeClick,
                    onCreateAccountClick = mockOnCreateAccountClick
                )
            }
        }
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }
}

