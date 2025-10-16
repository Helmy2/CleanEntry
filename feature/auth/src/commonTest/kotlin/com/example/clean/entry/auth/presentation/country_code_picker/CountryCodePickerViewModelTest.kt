package com.example.clean.entry.auth.presentation.country_code_picker

import com.example.clean.entry.auth.domain.model.Country
import com.example.clean.entry.auth.domain.repository.CountryRepository
import com.example.clean.entry.auth.navigation.CounterCodeResult
import com.example.clean.entry.core.navigation.AppNavigator
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@ExperimentalCoroutinesApi
class CountryCodePickerViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockedCountryRepository: CountryRepository
    private lateinit var mockedNavigator: AppNavigator

    private lateinit var viewModel: CountryCodePickerViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockedCountryRepository = mock()
        mockedNavigator = mock()
    }

    private fun createViewModel(countryCode: String? = null): CountryCodePickerViewModel {
        return CountryCodePickerViewModel(
            countryCode = countryCode,
            countryRepository = mockedCountryRepository,
            navigator = mockedNavigator
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given CountrySelectedCode event when getCountry succeeds then navigateBackWithResult`() =
        runTest {
            // Arrange
            val country = Country.Egypt
            everySuspend { mockedCountryRepository.getCountry(country.code) } returns Result.success(
                country
            )
            every { mockedNavigator.navigateBackWithResult(CounterCodeResult(country)) } returns Unit
            viewModel = createViewModel()

            // Act
            viewModel.handleEvent(CountryCodePickerReducer.Event.CountrySelectedCode(country.code))
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            verifySuspend { mockedCountryRepository.getCountry(country.code) }
            verify { mockedNavigator.navigateBackWithResult(CounterCodeResult(country)) }
        }

    @Test
    fun `given BackButtonClicked event then navigateBack is called`() = runTest {
        // Arrange
        every { mockedNavigator.navigateBack() } returns Unit
        viewModel = createViewModel()

        // Act
        viewModel.handleEvent(CountryCodePickerReducer.Event.BackButtonClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify { mockedNavigator.navigateBack() }
    }
}
