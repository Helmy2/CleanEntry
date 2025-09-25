package com.example.clean.entry.auth.domain.usecase

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ValidateEmailUseCaseTest {

    private lateinit var validateEmailUseCase: ValidateEmailUseCase

    @BeforeTest
    fun setUp() {
        validateEmailUseCase = ValidateEmailUseCase()
    }

    @Test
    fun `given empty email when invoke then returns failed result and not empty message`() {
        val email = ""

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals(
            "Email cannot be empty.",
            result.errorMessage
        )
    }

    @Test
    fun `given blank email when invoke then returns failed result and not empty message`() {
        val email = "   "

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals(
            "Email cannot be empty.",
            result.errorMessage?.toString()
        )
    }

    @Test
    fun `given valid email and pattern matches when invoke then returns successful result and null error message`() {
        val email = "test@example.com"

        val result = validateEmailUseCase(email)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given invalid email and pattern does not match when invoke then returns failed result and invalid email message`() {
        val email = "invalidemail"

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals(
            "That's not a valid email.",
            result.errorMessage
        )
    }

    @Test
    fun `given email missing at symbol and pattern does not match when invoke then returns failed result and invalid email message`() {
        val email = "testexample.com"

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals("That's not a valid email.", result.errorMessage.toString())
    }

    @Test
    fun `given email missing domain and pattern does not match when invoke then returns failed result and invalid email message`() {
        val email = "test@"

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals("That's not a valid email.", result.errorMessage.toString())
    }

    @Test
    fun `given email missing top level domain and pattern does not match when invoke then returns failed result and invalid email message`() {
        val email = "test@example"

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals("That's not a valid email.", result.errorMessage.toString())
    }

    @Test
    fun `given email missing username and pattern does not match when invoke then returns failed result and invalid email message`() {
        val email = "@example.com"

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals("That's not a valid email.", result.errorMessage.toString())
    }

    @Test
    fun `given email containing spaces and pattern does not match when invoke then returns failed result and invalid email message`() {
        val email = "test @example.com"

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals("That's not a valid email.", result.errorMessage.toString())
    }

    @Test
    fun `given email containing multiple at symbols and pattern does not match when invoke then returns failed result and invalid email message`() {
        val email = "test@@example.com"

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals("That's not a valid email.", result.errorMessage.toString())
    }

    @Test
    fun `given valid email containing numbers and pattern matches when invoke then returns successful result and null error message`() {
        val email = "test123@example.com"

        val result = validateEmailUseCase(email)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given valid email containing hyphen in local part and pattern matches when invoke then returns successful result and null error message`() {
        val email = "test-user@example.com"

        val result = validateEmailUseCase(email)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given valid email containing hyphen in domain and pattern matches when invoke then returns successful result and null error message`() {
        val email = "test@example-domain.com"

        val result = validateEmailUseCase(email)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given valid email containing dot in local part and pattern matches when invoke then returns successful result and null error message`() {
        val email = "test.user@example.com"

        val result = validateEmailUseCase(email)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given valid email with plus addressing and pattern matches when invoke then returns successful result and null error message`() {
        val email = "test+alias@example.com"

        val result = validateEmailUseCase(email)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given very long valid email and pattern matches when invoke then returns successful result and null error message`() {
        val email = "very.long.email.address.that.is.still.valid.according.to.standards@example.co.uk"

        val result = validateEmailUseCase(email)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given email containing special characters not allowed and pattern does not match when invoke then returns failed result and invalid email message`() {
        val email = "test!user@example.com"

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals("That's not a valid email.", result.errorMessage.toString())
    }

    @Test
    fun `given email domain starting with dot and pattern does not match when invoke then returns failed result and invalid email message`() {
        val email = "test@.example.com"

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals("That's not a valid email.", result.errorMessage.toString())
    }

    @Test
    fun `given email domain ending with dot and pattern does not match when invoke then returns failed result and invalid email message`() {
        val email = "test@example.com."

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals("That's not a valid email.", result.errorMessage.toString())
    }

    @Test
    fun `given email domain with consecutive dots and pattern does not match when invoke then returns failed result and invalid email message`() {
        val email = "test@example..com"

        val result = validateEmailUseCase(email)
        assertFalse(result.isSuccessful)
        assertEquals("That's not a valid email.", result.errorMessage.toString())
    }
}
