package com.example.clean.entry.auth.domain.usecase


import com.example.clean.entry.core.util.PhoneNumberVerifier
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ValidatePhoneUseCaseTest {

    private lateinit var phoneValidator: PhoneNumberVerifier
    private lateinit var validatePhoneUseCase: ValidatePhoneUseCase

    @BeforeTest
    fun setUp() {
        phoneValidator = mock()
        validatePhoneUseCase = ValidatePhoneUseCaseImpl(phoneValidator)
    }

    @Test
    fun `given blank phone when invoke then returns failed result and not empty message`() {
        val phone = ""
        val regionCode = "US"
        val result = validatePhoneUseCase(phone, regionCode)

        assertFalse(result.isSuccessful)
        assertEquals(
            "Phone number cannot be empty.",
            result.errorMessage
        )
    }

    @Test
    fun `given valid phone and region when invoke then returns successful result and null error message`() {
        val phone = "1234567890"
        val regionCode = "US"

        every { phoneValidator.isValidNumber(phone, regionCode) } returns true

        val result = validatePhoneUseCase(phone, regionCode)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given invalid phone for region when invoke then returns failed result and invalid for region message`() {
        val phone = "123"
        val regionCode = "US"

        every { phoneValidator.isValidNumber(phone, regionCode) } returns false

        val result = validatePhoneUseCase(phone, regionCode)

        assertFalse(result.isSuccessful)
        assertEquals(
            "Invalid phone number for the selected region.",
            result.errorMessage
        )
    }

    @Test
    fun `given phone causing parse exception when invoke then returns failed result and invalid format message`() {
        val phone = "invalidFormat"
        val regionCode = "US"

        every { phoneValidator.isValidNumber(phone, regionCode) } throws Exception(
            "Test Error"
        )

        val result = validatePhoneUseCase(phone, regionCode)

        assertFalse(result.isSuccessful)
        assertEquals(
            "Invalid phone number format.",
            result.errorMessage
        )
    }

    @Test
    fun `given valid phone and different region when invoke then returns successful result and null error message`() {
        val phone = "0791234567"
        val regionCode = "GB"

        every { phoneValidator.isValidNumber(phone, regionCode) } returns true

        val result = validatePhoneUseCase(phone, regionCode)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `given phone valid for one region but invalid for parse region when invoke then returns failed result and invalid for region message`() {
        val phone = "0791234567"
        val regionCode = "US"

        every { phoneValidator.isValidNumber(phone, regionCode) } returns false

        val result = validatePhoneUseCase(phone, regionCode)

        assertFalse(result.isSuccessful)
        assertEquals(
            "Invalid phone number for the selected region.",
            result.errorMessage
        )
    }
}