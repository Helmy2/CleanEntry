package com.example.clean.entry.feature_auth.domain.usecase


import com.example.clean.entry.core.domain.model.StringResource
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber
import io.mockk.every
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ValidatePhoneUseCaseTest {

    private lateinit var mockPhoneNumberUtil: PhoneNumberUtil
    private lateinit var validatePhoneUseCase: ValidatePhoneUseCase

    @BeforeTest
    fun setUp() {
        mockPhoneNumberUtil = mockk()
        validatePhoneUseCase = ValidatePhoneUseCase(mockPhoneNumberUtil)
    }

    @Test
    fun `invoke with blank phone returns failed result`() {
        val phone = ""
        val regionCode = "US"
        val result = validatePhoneUseCase(phone, regionCode)

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Phone number cannot be empty.").toString(),
            result.errorMessage?.toString()
        )
    }

    @Test
    fun `invoke with valid phone and region returns successful result`() {
        val phone = "1234567890"
        val regionCode = "US"
        val mockPhoneNumberProto = Phonenumber.PhoneNumber()

        every { mockPhoneNumberUtil.parse(phone, regionCode) } returns mockPhoneNumberProto
        every { mockPhoneNumberUtil.isValidNumber(mockPhoneNumberProto) } returns true

        val result = validatePhoneUseCase(phone, regionCode)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `invoke with invalid phone for region returns failed result`() {
        val phone = "123"
        val regionCode = "US"
        val mockPhoneNumberProto = Phonenumber.PhoneNumber()

        every { mockPhoneNumberUtil.parse(phone, regionCode) } returns mockPhoneNumberProto
        every { mockPhoneNumberUtil.isValidNumber(mockPhoneNumberProto) } returns false

        val result = validatePhoneUseCase(phone, regionCode)

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Invalid phone number for the selected region.").toString(),
            result.errorMessage?.toString()
        )
    }

    @Test
    fun `invoke with phone that causes parsing exception returns failed result`() {
        val phone = "invalidFormat"
        val regionCode = "US"

        every { mockPhoneNumberUtil.parse(phone, regionCode) } throws NumberParseException(
            NumberParseException.ErrorType.NOT_A_NUMBER,
            "Test Error"
        )

        val result = validatePhoneUseCase(phone, regionCode)

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Invalid phone number format.").toString(),
            result.errorMessage?.toString()
        )
    }

    @Test
    fun `invoke with valid phone and different region returns successful result`() {
        val phone = "0791234567"
        val regionCode = "GB"
        val mockPhoneNumberProto = Phonenumber.PhoneNumber()

        every { mockPhoneNumberUtil.parse(phone, regionCode) } returns mockPhoneNumberProto
        every { mockPhoneNumberUtil.isValidNumber(mockPhoneNumberProto) } returns true

        val result = validatePhoneUseCase(phone, regionCode)

        assertTrue(result.isSuccessful)
        assertNull(result.errorMessage)
    }

    @Test
    fun `invoke with phone valid for one region but invalid for parse region returns failed result`() {
        val phone = "0791234567"
        val regionCode = "US"
        val mockPhoneNumberProto = Phonenumber.PhoneNumber()

        every { mockPhoneNumberUtil.parse(phone, regionCode) } returns mockPhoneNumberProto
        every { mockPhoneNumberUtil.isValidNumber(mockPhoneNumberProto) } returns false // Key assumption

        val result = validatePhoneUseCase(phone, regionCode)

        assertFalse(result.isSuccessful)
        assertEquals(
            StringResource.FromString("Invalid phone number for the selected region.").toString(),
            result.errorMessage?.toString()
        )
    }
}
