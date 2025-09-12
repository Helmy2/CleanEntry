package com.example.clean.entry.feature.auth.domain.usecase

import com.example.clean.entry.core.domain.model.StringResource
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.toString

class ValidateSurnameUseCaseTest {

    private lateinit var validateSurnameUseCase: ValidateSurnameUseCase

    @BeforeTest
    fun setUp() {
        validateSurnameUseCase = ValidateSurnameUseCase()
    }

    @Test
    fun `given empty surname when invoke then returns failed result and not empty message`() {
        val surname = ""
        val result = validateSurnameUseCase(surname)

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Surname cannot be empty.").toString(),
            result.errorMessage?.toString()
        )
    }

    @Test
    fun `given blank surname with spaces only when invoke then returns failed result and not empty message`() {
        val surname = "   "
        val result = validateSurnameUseCase(surname)

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Surname cannot be empty.").toString(),
            result.errorMessage?.toString()
        )
    }

    @Test
    fun `given surname of one character when invoke then returns failed result and min length message`() {
        val surname = "A"
        val result = validateSurnameUseCase(surname)

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Surname must be at least 2 characters long.").toString(),
            result.errorMessage?.toString()
        )
    }

    @Test
    fun `given surname of two characters when invoke then returns successful result and null error message`() {
        val surname = "Ab"
        val result = validateSurnameUseCase(surname)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given surname longer than two characters when invoke then returns successful result and null error message`() {
        val surname = "Doe"
        val result = validateSurnameUseCase(surname)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given surname containing numbers and valid length when invoke then returns successful result and null error message`() {
        val surname = "Do3"
        val result = validateSurnameUseCase(surname)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given surname containing special chars and valid length when invoke then returns successful result and null error message`() {
        val surname = "O'Neil"
        val result = validateSurnameUseCase(surname)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given surname with leading trailing spaces and trimmed to invalid length when invoke then returns failed result and min length message`() {
        val surname = " D "
        val result = validateSurnameUseCase(surname.trim())

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Surname must be at least 2 characters long.").toString(),
            result.errorMessage?.toString()
        )
    }
}
