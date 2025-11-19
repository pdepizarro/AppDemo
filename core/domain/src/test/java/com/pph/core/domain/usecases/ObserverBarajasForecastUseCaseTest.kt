package com.pph.core.domain.usecases

import com.pph.domain.model.DailyForecastBo
import com.pph.domain.model.TempBo
import com.pph.domain.model.WeatherDescriptionBo
import com.pph.domain.repository.ForecastRepository
import com.pph.domain.usecases.ObserveBarajasForecastUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class ObserveBarajasForecastUseCaseTest {

    @MockK
    lateinit var repository: ForecastRepository

    private lateinit var useCase: ObserveBarajasForecastUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = ObserveBarajasForecastUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke should return flow with forecast list from repository`() = runTest {
        // Given
        val bo1 = DailyForecastBo(
            dateEpochDay = 20_000L,
            dt = 1_700_000_000L,
            humidity = 70,
            windSpeed = 4.0,
            temp = TempBo(
                day = 20.0,
                min = 18.0,
                max = 23.0,
                night = 19.0
            ),
            weather = listOf(
                WeatherDescriptionBo(
                    id = 800,
                    main = "Clear",
                    description = "clear sky",
                    icon = "01d"
                )
            )
        )

        val bo2 = bo1.copy(
            dateEpochDay = 20_001L,
            dt = 1_700_086_400L
        )

        every { repository.observeBarajasDailyForecast() } returns flowOf(listOf(bo1, bo2))

        // When
        val result: List<DailyForecastBo> = useCase().first()

        // Then
        assertEquals(2, result.size)
        assertEquals(bo1.dt, result[0].dt)
        assertEquals(bo2.dt, result[1].dt)
        verify(exactly = 1) { repository.observeBarajasDailyForecast() }
    }

    @Test
    fun `invoke should emit empty list when repository flow is empty`() = runTest {
        // Given
        every { repository.observeBarajasDailyForecast() } returns flowOf(emptyList())

        // When
        val result: List<DailyForecastBo> = useCase().first()

        // Then
        assertTrue(result.isEmpty())
        verify(exactly = 1) { repository.observeBarajasDailyForecast() }
    }
}
