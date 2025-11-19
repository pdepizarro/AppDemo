package com.pph.core.domain.usecases

import com.pph.domain.repository.ForecastRepository
import com.pph.domain.usecases.RefreshBarajasForecastUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.fail

class RefreshBarajasForecastUseCaseTest {

    @MockK
    lateinit var repository: ForecastRepository

    private lateinit var useCase: RefreshBarajasForecastUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = RefreshBarajasForecastUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke should delegate call to repository`() = runTest {
        // Given
        coEvery { repository.refreshBarajasForecast() } returns Result.success(Unit)

        // When
        val result = useCase()

        // Then
        // We only verify the delegation, no need to inspect Result internals here
        coVerify(exactly = 1) { repository.refreshBarajasForecast() }
        // Optional: you can touch the result so the value is actually used
        result.getOrNull()
    }

    @Test
    fun `invoke should rethrow CancellationException from repository`() = runTest {
        // Given
        val cancellation = CancellationException("Cancelled")
        coEvery { repository.refreshBarajasForecast() } throws cancellation

        try {
            // When
            useCase()
            fail("Expected CancellationException to be thrown")
        } catch (e: CancellationException) {
            // Then
        }

        coVerify(exactly = 1) { repository.refreshBarajasForecast() }
    }
}
