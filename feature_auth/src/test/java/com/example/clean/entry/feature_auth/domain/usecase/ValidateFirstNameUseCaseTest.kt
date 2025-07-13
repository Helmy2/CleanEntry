package com.example.clean.entry.feature_auth.domain.usecase

import com.example.clean.entry.util.StringResource
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidateFirstNameUseCaseTest {

    private lateinit var validateFirstNameUseCase:ValidateFirstNameUseCase

    @BeforeTest
    fun setUp() {
        validateFirstNameUseCase = ValidateFirstNameUseCase()
    }

    @Test
    fun `invoke with empty string returns unsuccessful result`() {
        val result = validateFirstNameUseCase("")
        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Name cannot be empty."),
            result.errorMessage
        )
    }

    @Test
    fun `invoke with string containing only spaces returns unsuccessful result`() {
        val result = validateFirstNameUseCase("   ")
        assertFalse(result.isSuccessful)
        assertEquals(StringResource.FromString("Name cannot be empty."), result.errorMessage)
    }

    @Test
    fun `invoke with single character string returns unsuccessful result`() {
        val result = validateFirstNameUseCase("a")
        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Name must be at least 2 characters long."),
            result.errorMessage
        )
    }

    @Test
    fun `invoke with two character string returns successful result`() {
        val result = validateFirstNameUseCase("ab")
        assertTrue(result.isSuccessful)
        assertEquals(null, result.errorMessage)
    }

    @Test
    fun `invoke with string longer than two characters returns successful result`() {
        val result = validateFirstNameUseCase("abc")
        assertTrue(result.isSuccessful)
        assertEquals(null, result.errorMessage)
    }

    @Test
    fun `invoke with string containing leading trailing spaces and valid length returns successful result`() {
        val result = validateFirstNameUseCase("  ab  ")
        assertTrue(result.isSuccessful)
        assertEquals(null, result.errorMessage)
    }

    @Test
    fun `invoke with string containing numbers and valid length returns successful result`() {
        val result = validateFirstNameUseCase("ab12")
        assertTrue(result.isSuccessful)
        assertEquals(null, result.errorMessage)
    }

    @Test
    fun `invoke with string containing special characters and valid length returns successful result`() {
        val result = validateFirstNameUseCase("ab!@")
        assertTrue(result.isSuccessful)
        assertEquals(null, result.errorMessage)
    }

    @Test
    fun `ValidateFirstNameUseCase invoke with string containing mixed case characters and valid length`() {
        val result = validateFirstNameUseCase("aBcD")
        assertTrue(result.isSuccessful)
        assertEquals(null, result.errorMessage)
    }

}