package com.example.clean.entry.feature_auth.domain.usecase

import com.example.clean.entry.core.domain.model.StringResource
import io.mockk.every
import io.mockk.mockk
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ValidateEmailUseCaseTest {

    private lateinit var mockEmailPattern: Pattern
    private lateinit var mockEmailMatcher: Matcher

    private lateinit var validateEmailUseCase: ValidateEmailUseCase

    @BeforeTest
    fun setUp() {
        mockEmailPattern = mockk()
        mockEmailMatcher = mockk()
        validateEmailUseCase = ValidateEmailUseCase(mockEmailPattern)
    }


    @Test
    fun `invoke with empty email returns failed result - Email cannot be empty`() {
        val email = ""

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Email cannot be empty.").toString(),
            result.errorMessage?.toString()
        )
    }

    @Test
    fun `invoke with blank email (spaces only) returns failed result - Email cannot be empty`() {
        val email = "   "

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Email cannot be empty.").toString(),
            result.errorMessage?.toString()
        )
    }

    @Test
    fun `invoke with valid email format (pattern matches) returns successful result`() {
        val email = "test@example.com"
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns true

        val result = validateEmailUseCase(email)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `invoke with invalid email format (pattern does not match) returns failed result - That's not a valid email`() {
        val email = "invalidemail"
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns false

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("That's not a valid email.").toString(),
            result.errorMessage?.toString()
        )
    }

    @Test
    fun `invoke with email missing at symbol (pattern does not match) returns failed result`() {
        val email = "testexample.com"
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns false

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals(StringResource.FromString("That's not a valid email.").toString(), result.errorMessage.toString())
    }

    @Test
    fun `invoke with email missing domain (pattern does not match) returns failed result`() {
        val email = "test@"
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns false

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals(StringResource.FromString("That's not a valid email.").toString(), result.errorMessage.toString())
    }

    @Test
    fun `invoke with email missing top-level domain (pattern does not match) returns failed result`() {
        val email = "test@example"
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns false

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals(StringResource.FromString("That's not a valid email.").toString(), result.errorMessage.toString())
    }

    @Test
    fun `invoke with email missing username (pattern does not match) returns failed result`() {
        val email = "@example.com"
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns false

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals(StringResource.FromString("That's not a valid email.").toString(), result.errorMessage.toString())
    }

    @Test
    fun `invoke with email containing spaces (pattern does not match) returns failed result`() {
        val email = "test @example.com"
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns false

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals(StringResource.FromString("That's not a valid email.").toString(), result.errorMessage.toString())
    }

    @Test
    fun `invoke with email containing multiple at symbols (pattern does not match) returns failed result`() {
        val email = "test@@example.com"
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns false

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals(StringResource.FromString("That's not a valid email.").toString(), result.errorMessage.toString())
    }

    @Test
    fun `invoke with valid email containing numbers (pattern matches) returns successful result`() {
        val email = "test123@example.com"
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns true

        val result = validateEmailUseCase(email)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `invoke with valid email containing hyphen in local part (pattern matches) returns successful result`() {
        val email = "test-user@example.com"
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns true

        val result = validateEmailUseCase(email)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `invoke with valid email containing hyphen in domain (pattern matches) returns successful result`() {
        val email = "test@example-domain.com"
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns true

        val result = validateEmailUseCase(email)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `invoke with valid email containing dot in local part (pattern matches) returns successful result`() {
        val email = "test.user@example.com"
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns true

        val result = validateEmailUseCase(email)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `invoke with valid email plus addressing (pattern matches) returns successful result`() {
        val email = "test+alias@example.com"
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns true

        val result = validateEmailUseCase(email)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `invoke with very long but valid email (pattern matches) returns successful result`() {
        val email = "very.long.email.address.that.is.still.valid.according.to.standards@example.co.uk"
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns true

        val result = validateEmailUseCase(email)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `invoke with email containing special characters not allowed (pattern does not match) returns failed result`() {
        val email = "test!user@example.com"
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns false

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals(StringResource.FromString("That's not a valid email.").toString(), result.errorMessage.toString())
    }

    @Test
    fun `invoke with email domain starting with dot (pattern does not match) returns failed result`() {
        val email = "test@.example.com"
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns false

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals(StringResource.FromString("That's not a valid email.").toString(), result.errorMessage.toString())
    }

    @Test
    fun `invoke with email domain ending with dot (pattern does not match) returns failed result`() {
        val email = "test@example.com."
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns false

        val result = validateEmailUseCase(email)

        assertFalse(result.isSuccessful)
        assertEquals(StringResource.FromString("That's not a valid email.").toString(), result.errorMessage.toString())
    }

    @Test
    fun `invoke with email domain with consecutive dots (pattern does not match) returns failed result`() {
        val email = "test@example..com"
        every { mockEmailPattern.matcher(email) } returns mockEmailMatcher
        every { mockEmailMatcher.matches() } returns false

        val result = validateEmailUseCase(email)
        assertFalse(result.isSuccessful)
        assertEquals(StringResource.FromString("That's not a valid email.").toString(), result.errorMessage.toString())
    }
}
