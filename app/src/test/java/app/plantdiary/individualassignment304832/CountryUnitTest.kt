package app.plantdiary.individualassignment304832

import org.junit.Test

import org.junit.Assert.*

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.plantdiary.individualassignment304832.MainViewModel
import app.plantdiary.individualassignment304832.dto.Country
import app.plantdiary.individualassignment304832.service.CountryService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After

import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * IMPORTANT!  YOU SHOULD NOT MAKE ANY CHANGES TO THIS FILE AS PART OF THE INDIVIDUAL ASSIGNMENT.
 * You can make this file compile and run by adding, and making changes to, the files that this unit test tests.
 *
 * Test the country logic.
 * Validate that the DTO works as expected.
 * Validate the format of the DTO string.
 * Validate that the fetch contains a minimum number of records.
 * Validate that "Belize" is one of the countries returned.
 */
class CountryUnitTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var mvm: MainViewModel
    lateinit var countryService: CountryService
    var allCountries : List<Country>? = ArrayList<Country>()

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @MockK
    lateinit var mockCountryService: CountryService

    @Before
    fun populateCountries() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockKAnnotations.init(this)
        mvm = MainViewModel()

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `given a country dto when code is NZ and name is New Zealand then code is NZ and name is New Zealand`() {
        var country = Country("NZ", "New Zealand")
        assertTrue(country.code.equals("NZ") )
        assertTrue(country.name.equals("New Zealand"))
    }

    @Test
    fun `given a country dto when code is NZ and name is New Zealand then output is New Zealand NZ`() {
        var country = Country("NZ", "New Zealand")
        assertTrue(country.toString().equals("New Zealand NZ"))
    }

    @Test
    fun `Given service connects to Countries JSON stream when data are read and parsed then country collection should be greater than zero`() =runTest {
        launch(Dispatchers.Main) {
            givenCountryServiceIsInitialized()
            whenServiceDataAreReadAndParsed()
            thenTheCountryCollectionSizeShouldBeGreaterThanZero()
        }
    }

    private fun givenCountryServiceIsInitialized() {
        countryService = CountryService()
    }

    private suspend fun whenServiceDataAreReadAndParsed() {
        allCountries = countryService.fetchCountries()
    }

    private fun thenTheCountryCollectionSizeShouldBeGreaterThanZero() {
        assertNotNull(allCountries)
        assertTrue(allCountries!!.isNotEmpty())
    }

    @Test
    fun `given a view model with live data when populated with countries then results should contain Belize`() {
            givenViewModelIsInitializedWithMockData()
            whenJSONDataAreReadAndParsed()
            thenResultsShouldContainBelize()
    }

    private fun givenViewModelIsInitializedWithMockData() {
        val countries = ArrayList<Country>()
        countries.add(Country("BZ", "Belize"))
        countries.add(Country("GB", "United Kingdom"))
        countries.add(Country("US", "United States"))

        coEvery {mockCountryService.fetchCountries()} returns countries

        mvm.countryService = mockCountryService
    }

    private fun whenJSONDataAreReadAndParsed() {
        mvm.fetchCountries()
    }

    private fun thenResultsShouldContainBelize() {
        var allCountries : List<Country>? = ArrayList<Country>()
        val latch = CountDownLatch(1);
        val observer = object : Observer<List<Country>> {
            override fun onChanged(t: List<Country>?) {
                allCountries = t
                latch.countDown()
                mvm.countries.removeObserver(this)
            }
        }
        mvm.countries.observeForever(observer)

        latch.await(1, TimeUnit.SECONDS)
        assertNotNull(allCountries)
        assertTrue(allCountries!!.contains(Country("BZ", "Belize")))

    }
}