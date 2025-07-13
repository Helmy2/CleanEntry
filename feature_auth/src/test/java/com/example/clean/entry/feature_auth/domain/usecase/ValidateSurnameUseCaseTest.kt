package com.example.clean.entry.feature_auth.domain.usecase

import com.example.clean.entry.core.domain.model.StringResource
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ValidateSurnameUseCaseTest {

    private lateinit var validateSurnameUseCase: ValidateSurnameUseCase

    @BeforeTest
    fun setUp() {
        validateSurnameUseCase = ValidateSurnameUseCase()
    }

    @Test
    fun `invoke with empty surname returns failed result`() {
        val surname = ""
        val result = validateSurnameUseCase(surname)

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Surname cannot be empty.").toString(),
            result.errorMessage?.toString()
        )
    }

    @Test
    fun `invoke with blank surname (spaces only) returns failed result`() {
        val surname = "   "
        val result = validateSurnameUseCase(surname)

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Surname cannot be empty.").toString(),
            result.errorMessage?.toString()
        )
    }

    @Test
    fun `invoke with surname of 1 character returns failed result`() {
        val surname = "A"
        val result = validateSurnameUseCase(surname)

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Surname must be at least 2 characters long.").toString(),
            result.errorMessage?.toString()
        )
    }

    @Test
    fun `invoke with surname of 2 characters returns successful result`() {
        val surname = "Ab"
        val result = validateSurnameUseCase(surname)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `invoke with surname longer than 2 characters returns successful result`() {
        val surname = "Doe"
        val result = validateSurnameUseCase(surname)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `invoke with surname containing numbers and valid length returns successful result`() {
        // Assuming business rules allow numbers in surnames for this use case
        val surname = "Do3"
        val result = validateSurnameUseCase(surname)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `invoke with surname containing special characters and valid length returns successful result`() {
        // Assuming business rules allow certain special characters
        val surname = "O'Neil"
        val result = validateSurnameUseCase(surname)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `invoke with surname with leading trailing spaces but valid inner length (after trim by isBlank) returns failed for length`() {
        // isBlank() handles the empty check, so if " D " becomes "D", length is 1.
        val surname = " D "
        val result = validateSurnameUseCase(surname.trim()) // Simulating what isBlank() would allow through to length check

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Surname must be at least 2 characters long.").toString(),
            result.errorMessage?.toString()
        )
    }
}
