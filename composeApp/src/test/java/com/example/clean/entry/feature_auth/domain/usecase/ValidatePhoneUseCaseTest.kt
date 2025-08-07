//package com.example.clean.entry.feature_auth.domain.usecase
//
//
//import com.example.clean.entry.core.domain.model.StringResource
//import io.michaelrocks.libphonenumber.android.NumberParseException
//import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
//import io.michaelrocks.libphonenumber.android.Phonenumber
//import io.mockk.every
//import io.mockk.mockk
//import kotlin.test.BeforeTest
//import kotlin.test.Test
//import kotlin.test.assertEquals
//import kotlin.test.assertFalse
//import kotlin.test.assertNull
//import kotlin.test.assertTrue
//
//class ValidatePhoneUseCaseTest {
//
//    private lateinit var mockPhoneNumberUtil: PhoneNumberUtil
//    private lateinit var validatePhoneUseCase: ValidatePhoneUseCase
//
//    @BeforeTest
//    fun setUp() {
//        mockPhoneNumberUtil = mockk()
//        validatePhoneUseCase = ValidatePhoneUseCase(mockPhoneNumberUtil)
//    }
//
//    @Test
//    fun `given blank phone when invoke then returns failed result and not empty message`() {
//        val phone = ""
//        val regionCode = "US"
//        val result = validatePhoneUseCase(phone, regionCode)
//
//        assertFalse(result.isSuccessful)
//        assertEquals(
//            StringResource.FromString("Phone number cannot be empty.").toString(),
//            result.errorMessage?.toString()
//        )
//    }
//
//    @Test
//    fun `given valid phone and region when invoke then returns successful result and null error message`() {
//        val phone = "1234567890"
//        val regionCode = "US"
//        val mockPhoneNumberProto = Phonenumber.PhoneNumber()
//
//        every { mockPhoneNumberUtil.parse(phone, regionCode) } returns mockPhoneNumberProto
//        every { mockPhoneNumberUtil.isValidNumber(mockPhoneNumberProto) } returns true
//
//        val result = validatePhoneUseCase(phone, regionCode)
//
//        assertTrue(result.isSuccessful)
//        assertNull(result.errorMessage)
//    }
//
//    @Test
//    fun `given invalid phone for region when invoke then returns failed result and invalid for region message`() {
//        val phone = "123"
//        val regionCode = "US"
//        val mockPhoneNumberProto = Phonenumber.PhoneNumber()
//
//        every { mockPhoneNumberUtil.parse(phone, regionCode) } returns mockPhoneNumberProto
//        every { mockPhoneNumberUtil.isValidNumber(mockPhoneNumberProto) } returns false
//
//        val result = validatePhoneUseCase(phone, regionCode)
//
//        assertFalse(result.isSuccessful)
//        assertEquals(
//            StringResource.FromString("Invalid phone number for the selected region.").toString(),
//            result.errorMessage?.toString()
//        )
//    }
//
//    @Test
//    fun `given phone causing parse exception when invoke then returns failed result and invalid format message`() {
//        val phone = "invalidFormat"
//        val regionCode = "US"
//
//        every { mockPhoneNumberUtil.parse(phone, regionCode) } throws NumberParseException(
//            NumberParseException.ErrorType.NOT_A_NUMBER,
//            "Test Error"
//        )
//
//        val result = validatePhoneUseCase(phone, regionCode)
//
//        assertFalse(result.isSuccessful)
//        assertEquals(
//            StringResource.FromString("Invalid phone number format.").toString(),
//            result.errorMessage?.toString()
//        )
//    }
//
//    @Test
//    fun `given valid phone and different region when invoke then returns successful result and null error message`() {
//        val phone = "0791234567"
//        val regionCode = "GB"
//        val mockPhoneNumberProto = Phonenumber.PhoneNumber()
//
//        every { mockPhoneNumberUtil.parse(phone, regionCode) } returns mockPhoneNumberProto
//        every { mockPhoneNumberUtil.isValidNumber(mockPhoneNumberProto) } returns true
//
//        val result = validatePhoneUseCase(phone, regionCode)
//
//        assertTrue(result.isSuccessful)
//        assertNull(result.errorMessage)
//    }
//
//    @Test
//    fun `given phone valid for one region but invalid for parse region when invoke then returns failed result and invalid for region message`() {
//        val phone = "0791234567"
//        val regionCode = "US"
//        val mockPhoneNumberProto = Phonenumber.PhoneNumber()
//
//        every { mockPhoneNumberUtil.parse(phone, regionCode) } returns mockPhoneNumberProto
//        every { mockPhoneNumberUtil.isValidNumber(mockPhoneNumberProto) } returns false
//
//        val result = validatePhoneUseCase(phone, regionCode)
//
//        assertFalse(result.isSuccessful)
//        assertEquals(
//            StringResource.FromString("Invalid phone number for the selected region.").toString(),
//            result.errorMessage?.toString()
//        )
//    }
//}
