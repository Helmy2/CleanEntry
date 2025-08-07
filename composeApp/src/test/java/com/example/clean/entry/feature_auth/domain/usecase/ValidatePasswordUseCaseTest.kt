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
    fun `given password shorter than 6 chars when invoke then returns failed result and min length message`() {
        val password = "12345"
        val result = validatePasswordUseCase(password)

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Password must be at least 6 characters long.").toString(),
            result.errorMessage?.toString()
        )
    }

    @Test
    fun `given password exactly 6 chars when invoke then returns successful result and null error message`() {
        val password = "123456"
        val result = validatePasswordUseCase(password)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given password longer than 6 chars when invoke then returns successful result and null error message`() {
        val password = "1234567"
        val result = validatePasswordUseCase(password)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given empty password when invoke then returns failed result and min length message`() {
        val password = ""
        val result = validatePasswordUseCase(password)

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Password must be at least 6 characters long.").toString(),
            result.errorMessage?.toString()
        )
    }

    @Test
    fun `given password with spaces and valid length when invoke then returns successful result and null error message`() {
        val password = "pass word"
        val result = validatePasswordUseCase(password)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given password with special chars and valid length when invoke then returns successful result and null error message`() {
        val password = "p@sswOrd!"
        val result = validatePasswordUseCase(password)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }
}
