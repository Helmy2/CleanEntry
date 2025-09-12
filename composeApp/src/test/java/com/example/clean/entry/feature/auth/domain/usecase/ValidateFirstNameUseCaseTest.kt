package com.example.clean.entry.feature.auth.domain.usecase

import com.example.clean.entry.core.domain.model.StringResource
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ValidateFirstNameUseCaseTest {

    private lateinit var validateFirstNameUseCase: ValidateFirstNameUseCase

    @BeforeTest
    fun setUp() {
        validateFirstNameUseCase = ValidateFirstNameUseCase()
    }

    @Test
    fun `given empty string when invoke then returns unsuccessful result and not empty message`() {
        val result = validateFirstNameUseCase("")
        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Name cannot be empty."),
            result.errorMessage
        )
    }

    @Test
    fun `given string containing only spaces when invoke then returns unsuccessful result and not empty message`() {
        val result = validateFirstNameUseCase("   ")
        assertFalse(result.isSuccessful)
        assertEquals(StringResource.FromString("Name cannot be empty."), result.errorMessage)
    }

    @Test
    fun `given single character string when invoke then returns unsuccessful result and min length message`() {
        val result = validateFirstNameUseCase("a")
        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Name must be at least 2 characters long."),
            result.errorMessage
        )
    }

    @Test
    fun `given two character string when invoke then returns successful result and null error message`() {
        val result = validateFirstNameUseCase("ab")
        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given string longer than two characters when invoke then returns successful result and null error message`() {
        val result = validateFirstNameUseCase("abc")
        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given string with leading trailing spaces and valid length when invoke then returns successful result and null error message`() {
        val result = validateFirstNameUseCase("  ab  ")
        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given string containing numbers and valid length when invoke then returns successful result and null error message`() {
        val result = validateFirstNameUseCase("ab12")
        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given string containing special characters and valid length when invoke then returns successful result and null error message`() {
        val result = validateFirstNameUseCase("ab!@")
        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given string containing mixed case characters and valid length when invoke then returns successful result and null error message`() {
        val result = validateFirstNameUseCase("aBcD")
        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }
}
