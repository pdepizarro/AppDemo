package com.pph.core.data.repository.impl

import com.pph.data.mapper.toDailyForecastEntities
import com.pph.data.model.dao.ForecastDao
import com.pph.data.model.dto.DailyForecastDto
import com.pph.data.model.dto.ForecastResponseDto
import com.pph.data.model.dto.TempDto
import com.pph.data.model.dto.WeatherDescriptionDto
import com.pph.data.model.entity.DailyForecastEntity
import com.pph.data.remote.api.ForecastApi
import com.pph.data.repository.impl.ForecastRepositoryImpl
import com.pph.domain.model.DailyForecastBo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifyOrder

class ForecastRepositoryImplTest {

    @MockK(relaxUnitFun = true)
    lateinit var api: ForecastApi

    @MockK(relaxUnitFun = true)
    lateinit var forecastDao: ForecastDao

    private lateinit var repository: ForecastRepositoryImpl

    private val apiKey = "TEST_API_KEY"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = ForecastRepositoryImpl(
            api = api,
            barajasForecastDao = forecastDao,
            apiKey = apiKey
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `observeBarajasDailyForecast should map entities to bos`() = runTest {
        // Given
        val entity1 = DailyForecastEntity(
            dateEpochDay = 20000L,
            dt = 1_700_000_000L,
            humidity = 70,
            windSpeed = 4.0,
            dayTemp = 20.0,
            minTemp = 18.0,
            maxTemp = 23.0,
            nightTemp = 19.0,
            weatherId = 800,
            weatherMain = "Clear",
            weatherDescription = "clear sky",
            weatherIcon = "01d",
            timezoneOffset = 0,
            lastUpdated = 1L
        )

        val entity2 = DailyForecastEntity(
            dateEpochDay = 20001L,
            dt = 1_700_086_400L,
            humidity = 65,
            windSpeed = 3.5,
            dayTemp = 18.0,
            minTemp = 16.0,
            maxTemp = 21.0,
            nightTemp = 17.0,
            weatherId = 801,
            weatherMain = "Clouds",
            weatherDescription = "few clouds",
            weatherIcon = "02d",
            timezoneOffset = 0,
            lastUpdated = 2L
        )

        every { forecastDao.getDailyForecast() } returns flowOf(listOf(entity1, entity2))

        // When
        val result: List<DailyForecastBo> = repository.observeBarajasDailyForecast().first()

        // Then
        assertEquals(2, result.size)
        assertEquals(entity1.dt, result[0].dt)
        assertEquals(entity2.dt, result[1].dt)
        verify(exactly = 1) { forecastDao.getDailyForecast() }
    }

    @Test
    fun `observeBarajasDailyForecast should emit empty list when dao returns empty list`() =
        runTest {
            // Given
            every { forecastDao.getDailyForecast() } returns flowOf(emptyList())

            // When
            val result: List<DailyForecastBo> = repository.observeBarajasDailyForecast().first()

            // Then
            assertTrue(result.isEmpty())
            verify(exactly = 1) { forecastDao.getDailyForecast() }
        }

    // -----------------------------------------------------------------------
    //  refreshBarajasForecast
    // -----------------------------------------------------------------------

    @Test
    fun `refreshBarajasForecast should call api, clear dao and insert mapped entities on success`() =
        runTest {
            // Given
            val dto = ForecastResponseDto(
                lat = 40.0,
                lon = -3.0,
                timezone = "Europe/Madrid",
                timezoneOffset = 3600,
                daily = listOf(
                    DailyForecastDto(
                        dt = 1_700_000_000L,
                        humidity = 80,
                        windSpeed = 5.0,
                        temp = TempDto(
                            day = 21.0,
                            min = 18.0,
                            max = 25.0,
                            night = 19.0
                        ),
                        weather = listOf(
                            WeatherDescriptionDto(
                                id = 800,
                                main = "Clear",
                                description = "clear sky",
                                icon = "01d"
                            )
                        )
                    ),
                    DailyForecastDto(
                        dt = 1_700_086_400L,
                        humidity = 70,
                        windSpeed = 4.0,
                        temp = TempDto(
                            day = 19.0,
                            min = 16.0,
                            max = 22.0,
                            night = 17.0
                        ),
                        weather = emptyList()
                    )
                )
            )

            coEvery { api.getForecast(apiKey = apiKey) } returns dto
            coEvery { forecastDao.clearDailyForecast() } returns Unit
            coEvery { forecastDao.insertDailyForecast(any()) } returns Unit

            // When
            val result = repository.refreshBarajasForecast()

            // Then
            assertTrue(result.isSuccess)

            coVerifyOrder {
                api.getForecast(apiKey = apiKey)
                forecastDao.clearDailyForecast()
                forecastDao.insertDailyForecast(withArg { entities ->
                    // We cannot assert exact timestamps, but we can assert size
                    assertEquals(dto.daily.size, entities.size)
                })
            }
        }

    @Test
    fun `refreshBarajasForecast should return failure when api throws generic exception`() =
        runTest {
            // Given
            val exception = IllegalStateException("Network error")
            coEvery { api.getForecast(apiKey = apiKey) } throws exception

            // When
            val result = repository.refreshBarajasForecast()

            // Then
            assertTrue(result.isFailure)
            assertSame(exception, result.exceptionOrNull())

            coVerify(exactly = 1) { api.getForecast(apiKey = apiKey) }
            coVerify(exactly = 0) { forecastDao.clearDailyForecast() }
            coVerify(exactly = 0) { forecastDao.insertDailyForecast(any()) }
        }

    @Test
    fun `refreshBarajasForecast should rethrow CancellationException and not touch dao`() =
        runTest {
            // Given
            val cancellation = CancellationException("Cancelled")
            coEvery { api.getForecast(apiKey = apiKey) } throws cancellation

            try {
                // When
                repository.refreshBarajasForecast()
                fail("Expected CancellationException to be thrown")
            } catch (e: CancellationException) {
                // Then
                assertSame(cancellation, e)
            }

            coVerify(exactly = 1) { api.getForecast(apiKey = apiKey) }
            coVerify(exactly = 0) { forecastDao.clearDailyForecast() }
            coVerify(exactly = 0) { forecastDao.insertDailyForecast(any()) }
        }
}
