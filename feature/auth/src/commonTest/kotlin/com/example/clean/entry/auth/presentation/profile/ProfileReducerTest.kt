package com.example.clean.entry.auth.presentation.profile

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProfileReducerTest {

    private val reducer = ProfileReducer
    private val initialState = ProfileReducer.State()

    @Test
    fun `given OnLogoutClicked event then isLoading is true`() {
        // Act
        val (newState, _) = reducer.reduce(initialState, ProfileReducer.Event.OnLogoutClicked)

        // Assert
        assertTrue(newState.isLoading)
    }

    @Test
    fun `given LogoutSucceeded event then state is reset`() {
        // Arrange
        val previousState = ProfileReducer.State(isLoading = true)

        // Act
        val (newState, _) = reducer.reduce(previousState, ProfileReducer.Event.LogoutSucceeded)

        // Assert
        assertEquals(initialState, newState)
    }

    @Test
    fun `given LogoutFailed event then isLoading is false and error is set`() {
        // Arrange
        val previousState = ProfileReducer.State(isLoading = true)
        val errorMessage = "Logout failed"
        val event = ProfileReducer.Event.LogoutFailed(errorMessage)

        // Act
        val (newState, _) = reducer.reduce(previousState, event)

        // Assert
        assertFalse(newState.isLoading)
        assertEquals(errorMessage, newState.error)
    }
}
