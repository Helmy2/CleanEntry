package com.example.clean.entry.feature_auth.domain.usecase

import com.example.clean.entry.core.domain.model.StringResource
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ValidatePasswordUseCaseTest {

    private lateinit var validatePasswordUseCase: ValidatePasswordUseCase

    @BeforeTest
    fun setUp() {
        validatePasswordUseCase = ValidatePasswordUseCase()
    }

    @Test
    fun `invoke with password shorter than 6 characters returns failed result`() {
        val password = "12345" // 5 characters
        val result = validatePasswordUseCase(password)

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Password must be at least 6 characters long.").toString(),
            result.errorMessage?.toString()
        )
    }

    @Test
    fun `invoke with password exactly 6 characters returns successful result`() {
        val password = "123456" // 6 characters
        val result = validatePasswordUseCase(password)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `invoke with password longer than 6 characters returns successful result`() {
        val password = "1234567" // 7 characters
        val result = validatePasswordUseCase(password)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `invoke with empty password returns failed result`() {
        val password = "" // 0 characters
        val result = validatePasswordUseCase(password)

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Password must be at least 6 characters long.").toString(),
            result.errorMessage?.toString()
        )
    }

    @Test
    fun `invoke with password containing spaces and valid length returns successful result`() {
        val password = "pass word" // 9 characters
        val result = validatePasswordUseCase(password)

        assertTrue(result.isSuccessful) // Assuming spaces count towards length
        assertNull(result.errorMessage)
    }

    @Test
    fun `invoke with password containing special characters and valid length returns successful result`() {
        val password = "p@sswOrd!" // 9 characters
        val result = validatePasswordUseCase(password)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }
}
